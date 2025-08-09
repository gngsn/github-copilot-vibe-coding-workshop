using System.Text.Json;
using Contoso.BlazorApp.Models;

namespace Contoso.BlazorApp.Services;

public interface IApiService
{
    Task<List<Post>> GetPostsAsync();
    Task<Post?> CreatePostAsync(CreatePostRequest request);
    Task<bool> CheckApiAvailabilityAsync();
}

public class ApiService : IApiService
{
    private readonly HttpClient _httpClient;
    private readonly JsonSerializerOptions _jsonOptions;

    public ApiService(IHttpClientFactory httpClientFactory)
    {
        _httpClient = httpClientFactory.CreateClient("API");
        _jsonOptions = new JsonSerializerOptions
        {
            PropertyNamingPolicy = JsonNamingPolicy.CamelCase,
            PropertyNameCaseInsensitive = true
        };
    }

    public async Task<List<Post>> GetPostsAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("posts");
            if (response.IsSuccessStatusCode)
            {
                var content = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<List<Post>>(content, _jsonOptions) ?? new List<Post>();
            }
            return new List<Post>();
        }
        catch
        {
            return new List<Post>();
        }
    }

    public async Task<Post?> CreatePostAsync(CreatePostRequest request)
    {
        try
        {
            var json = JsonSerializer.Serialize(request, _jsonOptions);
            var content = new StringContent(json, System.Text.Encoding.UTF8, "application/json");
            var response = await _httpClient.PostAsync("posts", content);
            
            if (response.IsSuccessStatusCode)
            {
                var responseContent = await response.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Post>(responseContent, _jsonOptions);
            }
            return null;
        }
        catch
        {
            return null;
        }
    }

    public async Task<bool> CheckApiAvailabilityAsync()
    {
        try
        {
            var response = await _httpClient.GetAsync("posts");
            return response.IsSuccessStatusCode;
        }
        catch
        {
            return false;
        }
    }
}

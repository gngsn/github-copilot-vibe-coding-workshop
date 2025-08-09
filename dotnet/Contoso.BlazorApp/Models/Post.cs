namespace Contoso.BlazorApp.Models;

public class Post
{
    public string Id { get; set; } = string.Empty;
    public string Username { get; set; } = string.Empty;
    public string Content { get; set; } = string.Empty;
    public DateTime CreatedAt { get; set; }
    public DateTime UpdatedAt { get; set; }
    public int Likes { get; set; }
    public int Comments { get; set; }
}

public class CreatePostRequest
{
    public string Username { get; set; } = string.Empty;
    public string Content { get; set; } = string.Empty;
}

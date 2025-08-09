import os
from fastapi import FastAPI, HTTPException, Request, Response, status
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse, FileResponse
from fastapi.openapi.docs import get_swagger_ui_html
from pydantic import BaseModel, Field
from typing import List, Optional
import sqlite3
from datetime import datetime

DB_PATH = os.path.join(os.path.dirname(__file__), "sns_api.db")
OPENAPI_PATH = os.path.join(os.path.dirname(__file__), "../openapi.yaml")

app = FastAPI(
    title="Simple Social Media Application API",
    version="1.0.0",
    description="API for a basic social networking service supporting posts, comments, and likes.",
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


def get_db():
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    return conn


def init_db():
    conn = get_db()
    cur = conn.cursor()
    cur.execute(
        """
    CREATE TABLE IF NOT EXISTS posts (
        id TEXT PRIMARY KEY,
        username TEXT NOT NULL,
        content TEXT NOT NULL,
        createdAt TEXT NOT NULL,
        updatedAt TEXT NOT NULL
    )
    """
    )
    cur.execute(
        """
    CREATE TABLE IF NOT EXISTS comments (
        id TEXT PRIMARY KEY,
        postId TEXT NOT NULL,
        username TEXT NOT NULL,
        content TEXT NOT NULL,
        createdAt TEXT NOT NULL,
        updatedAt TEXT NOT NULL
    )
    """
    )
    cur.execute(
        """
    CREATE TABLE IF NOT EXISTS likes (
        postId TEXT NOT NULL,
        username TEXT NOT NULL,
        createdAt TEXT NOT NULL,
        PRIMARY KEY (postId, username)
    )
    """
    )
    conn.commit()

    # Insert mock posts if not present
    cur.execute("SELECT COUNT(*) FROM posts")
    count = cur.fetchone()[0]
    if count == 0:
        from datetime import datetime

        now = datetime.utcnow().isoformat()
        posts = [
            ("1", "alice", "Hello world!", now, now),
            ("2", "bob", "This is my first post.", now, now),
            ("3", "carol", "Excited to join this community!", now, now),
        ]
        cur.executemany(
            "INSERT INTO posts (id, username, content, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)",
            posts,
        )
        conn.commit()
    conn.close()


@app.on_event("startup")
def startup():
    init_db()


# Models (from openapi.yaml)
class Post(BaseModel):
    id: str
    username: str
    content: str
    createdAt: str
    updatedAt: str
    likes: int
    comments: int


class PostCreate(BaseModel):
    username: str
    content: str


class PostUpdate(BaseModel):
    username: str
    content: str


class Comment(BaseModel):
    id: str
    postId: str
    username: str
    content: str
    createdAt: str
    updatedAt: str


class CommentCreate(BaseModel):
    username: str
    content: str


class CommentUpdate(BaseModel):
    username: str
    content: str


class Like(BaseModel):
    postId: str
    username: str
    createdAt: str


class LikeCreate(BaseModel):
    username: str


class Error(BaseModel):
    message: str


# Utility functions
def get_post_counts(post_id):
    conn = get_db()
    cur = conn.cursor()
    cur.execute("SELECT COUNT(*) FROM likes WHERE postId = ?", (post_id,))
    likes = cur.fetchone()[0]
    cur.execute("SELECT COUNT(*) FROM comments WHERE postId = ?", (post_id,))
    comments = cur.fetchone()[0]
    conn.close()
    return likes, comments


# Endpoints
@app.get("/posts", response_model=List[Post])
def list_posts():
    conn = get_db()
    cur = conn.cursor()
    cur.execute("SELECT * FROM posts ORDER BY createdAt DESC")
    posts = []
    for row in cur.fetchall():
        likes, comments = get_post_counts(row["id"])
        posts.append(Post(**dict(row), likes=likes, comments=comments))
    conn.close()
    return posts


@app.post("/posts", response_model=Post, status_code=201)
def create_post(post: PostCreate):
    conn = get_db()
    cur = conn.cursor()
    post_id = str(datetime.utcnow().timestamp()).replace(".", "")
    now = datetime.utcnow().isoformat()
    cur.execute(
        "INSERT INTO posts (id, username, content, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?)",
        (post_id, post.username, post.content, now, now),
    )
    conn.commit()
    likes, comments = 0, 0
    conn.close()
    return Post(
        id=post_id,
        username=post.username,
        content=post.content,
        createdAt=now,
        updatedAt=now,
        likes=likes,
        comments=comments,
    )


@app.get("/posts/{postId}", response_model=Post)
def get_post(postId: str):
    conn = get_db()
    cur = conn.cursor()
    cur.execute("SELECT * FROM posts WHERE id = ?", (postId,))
    row = cur.fetchone()
    if not row:
        conn.close()
        raise HTTPException(status_code=404, detail="Post not found")
    likes, comments = get_post_counts(postId)
    conn.close()
    return Post(**dict(row), likes=likes, comments=comments)


@app.patch("/posts/{postId}", response_model=Post)
def update_post(postId: str, post: PostUpdate):
    conn = get_db()
    cur = conn.cursor()
    cur.execute("SELECT * FROM posts WHERE id = ?", (postId,))
    row = cur.fetchone()
    if not row:
        conn.close()
        raise HTTPException(status_code=404, detail="Post not found")
    now = datetime.utcnow().isoformat()
    cur.execute(
        "UPDATE posts SET username = ?, content = ?, updatedAt = ? WHERE id = ?",
        (post.username, post.content, now, postId),
    )
    conn.commit()
    likes, comments = get_post_counts(postId)
    conn.close()
    return Post(
        id=postId,
        username=post.username,
        content=post.content,
        createdAt=row["createdAt"],
        updatedAt=now,
        likes=likes,
        comments=comments,
    )


@app.delete("/posts/{postId}", status_code=204)
def delete_post(postId: str):
    conn = get_db()
    cur = conn.cursor()
    cur.execute("DELETE FROM posts WHERE id = ?", (postId,))
    conn.commit()
    conn.close()
    return Response(status_code=204)


@app.get("/posts/{postId}/comments", response_model=List[Comment])
def list_comments(postId: str):
    conn = get_db()
    cur = conn.cursor()
    cur.execute(
        "SELECT * FROM comments WHERE postId = ? ORDER BY createdAt ASC", (postId,)
    )
    comments = [Comment(**dict(row)) for row in cur.fetchall()]
    conn.close()
    return comments


@app.post("/posts/{postId}/comments", response_model=Comment, status_code=201)
def create_comment(postId: str, comment: CommentCreate):
    conn = get_db()
    cur = conn.cursor()
    comment_id = str(datetime.utcnow().timestamp()).replace(".", "")
    now = datetime.utcnow().isoformat()
    cur.execute(
        "INSERT INTO comments (id, postId, username, content, createdAt, updatedAt) VALUES (?, ?, ?, ?, ?, ?)",
        (comment_id, postId, comment.username, comment.content, now, now),
    )
    conn.commit()
    conn.close()
    return Comment(
        id=comment_id,
        postId=postId,
        username=comment.username,
        content=comment.content,
        createdAt=now,
        updatedAt=now,
    )


@app.get("/posts/{postId}/comments/{commentId}", response_model=Comment)
def get_comment(postId: str, commentId: str):
    conn = get_db()
    cur = conn.cursor()
    cur.execute(
        "SELECT * FROM comments WHERE id = ? AND postId = ?", (commentId, postId)
    )
    row = cur.fetchone()
    if not row:
        conn.close()
        raise HTTPException(status_code=404, detail="Comment not found")
    conn.close()
    return Comment(**dict(row))


@app.patch("/posts/{postId}/comments/{commentId}", response_model=Comment)
def update_comment(postId: str, commentId: str, comment: CommentUpdate):
    conn = get_db()
    cur = conn.cursor()
    cur.execute(
        "SELECT * FROM comments WHERE id = ? AND postId = ?", (commentId, postId)
    )
    row = cur.fetchone()
    if not row:
        conn.close()
        raise HTTPException(status_code=404, detail="Comment not found")
    now = datetime.utcnow().isoformat()
    cur.execute(
        "UPDATE comments SET username = ?, content = ?, updatedAt = ? WHERE id = ? AND postId = ?",
        (comment.username, comment.content, now, commentId, postId),
    )
    conn.commit()
    conn.close()
    return Comment(
        id=commentId,
        postId=postId,
        username=comment.username,
        content=comment.content,
        createdAt=row["createdAt"],
        updatedAt=now,
    )


@app.delete("/posts/{postId}/comments/{commentId}", status_code=204)
def delete_comment(postId: str, commentId: str):
    conn = get_db()
    cur = conn.cursor()
    cur.execute("DELETE FROM comments WHERE id = ? AND postId = ?", (commentId, postId))
    conn.commit()
    conn.close()
    return Response(status_code=204)


@app.post("/posts/{postId}/likes", response_model=Like, status_code=201)
def like_post(postId: str, like: LikeCreate):
    conn = get_db()
    cur = conn.cursor()
    now = datetime.utcnow().isoformat()
    try:
        cur.execute(
            "INSERT INTO likes (postId, username, createdAt) VALUES (?, ?, ?)",
            (postId, like.username, now),
        )
        conn.commit()
    except sqlite3.IntegrityError:
        conn.close()
        raise HTTPException(status_code=400, detail="Already liked")
    conn.close()
    return Like(postId=postId, username=like.username, createdAt=now)


@app.delete("/posts/{postId}/likes", status_code=204)
def unlike_post(postId: str, like: LikeCreate):
    conn = get_db()
    cur = conn.cursor()
    cur.execute(
        "DELETE FROM likes WHERE postId = ? AND username = ?", (postId, like.username)
    )
    conn.commit()
    conn.close()
    return Response(status_code=204)


@app.get("/openapi.yaml")
def get_openapi_yaml():
    return FileResponse(OPENAPI_PATH, media_type="text/yaml")


@app.get("/", include_in_schema=False)
def swagger_ui():
    return get_swagger_ui_html(openapi_url="/openapi.json", title=app.title)


if __name__ == "__main__":
    import uvicorn

    uvicorn.run(app, host="0.0.0.0", port=8000)

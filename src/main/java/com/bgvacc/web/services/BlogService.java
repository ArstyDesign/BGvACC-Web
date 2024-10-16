package com.bgvacc.web.services;

import com.bgvacc.web.models.portal.blog.posts.PostEditModel;
import com.bgvacc.web.responses.blog.PostResponse;
import java.util.List;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public interface BlogService {

  List<PostResponse> getVisibleBlogPosts();

  List<PostResponse> getBlogPosts();

  PostResponse getBlogPost(String postId);

  PostResponse editBlogPost(PostEditModel pem);

  boolean changeBlogPostVisibility(String postId, boolean isVisible);

}

package com.bgvacc.web.services;

import com.bgvacc.web.models.portal.blog.posts.PostEditModel;
import com.bgvacc.web.responses.blog.PostResponse;
import com.bgvacc.web.utils.Names;
import java.sql.*;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final JdbcTemplate jdbcTemplate;

  @Override
  public List<PostResponse> getVisibleBlogPosts() {

    final String getVisiblePostsSql = "SELECT * FROM blog_posts WHERE is_visible = true ORDER BY created_at DESC";
    final String getAuthorInfoSql = "SELECT first_name, last_name FROM users WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getVisiblePostsPstmt = conn.prepareStatement(getVisiblePostsSql);
            PreparedStatement getAuthorInfoPstmt = conn.prepareStatement(getAuthorInfoSql)) {

      try {

        conn.setAutoCommit(false);

        List<PostResponse> posts = new ArrayList<>();

        ResultSet getVisiblePostsRset = getVisiblePostsPstmt.executeQuery();

        while (getVisiblePostsRset.next()) {

          PostResponse post = new PostResponse();
          post.setId(getVisiblePostsRset.getString("id"));
          post.setTitle(getVisiblePostsRset.getString("title"));
          post.setContent(getVisiblePostsRset.getString("content"));
          post.setMainImage(getVisiblePostsRset.getString("main_image"));
          post.setVisible(getVisiblePostsRset.getBoolean("is_visible"));
          post.setCid(getVisiblePostsRset.getString("cid"));

          getAuthorInfoPstmt.setString(1, post.getCid());

          ResultSet getAuthorInfoRset = getAuthorInfoPstmt.executeQuery();

          if (getAuthorInfoRset.next()) {
            Names names = Names.builder().firstName(getAuthorInfoRset.getString("first_name")).lastName(getAuthorInfoRset.getString("last_name")).build();
            post.setNames(names);
          }

          post.setCreatedAt(getVisiblePostsRset.getTimestamp("created_at"));
          post.setUpdatedAt(getVisiblePostsRset.getTimestamp("updated_at"));

          posts.add(post);
        }

        return posts;

      } catch (SQLException ex) {
        log.error("Error getting visible posts", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting visible posts", e);
    }

    return null;
  }

  @Override
  public List<PostResponse> getBlogPosts() {

    final String getPostsSql = "SELECT * FROM blog_posts ORDER BY created_at DESC";
    final String getAuthorInfoSql = "SELECT first_name, last_name FROM users WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getPostsPstmt = conn.prepareStatement(getPostsSql);
            PreparedStatement getAuthorInfoPstmt = conn.prepareStatement(getAuthorInfoSql)) {

      try {

        conn.setAutoCommit(false);

        List<PostResponse> posts = new ArrayList<>();

        ResultSet getPostsRset = getPostsPstmt.executeQuery();

        while (getPostsRset.next()) {

          PostResponse post = new PostResponse();
          post.setId(getPostsRset.getString("id"));
          post.setTitle(getPostsRset.getString("title"));
          post.setContent(getPostsRset.getString("content"));
          post.setMainImage(getPostsRset.getString("main_image"));
          post.setVisible(getPostsRset.getBoolean("is_visible"));
          post.setCid(getPostsRset.getString("cid"));

          getAuthorInfoPstmt.setString(1, post.getCid());

          ResultSet getAuthorInfoRset = getAuthorInfoPstmt.executeQuery();

          if (getAuthorInfoRset.next()) {
            Names names = Names.builder().firstName(getAuthorInfoRset.getString("first_name")).lastName(getAuthorInfoRset.getString("last_name")).build();
            post.setNames(names);
          }

          post.setCreatedAt(getPostsRset.getTimestamp("created_at"));
          post.setUpdatedAt(getPostsRset.getTimestamp("updated_at"));

          posts.add(post);
        }

        return posts;

      } catch (SQLException ex) {
        log.error("Error getting posts", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting posts", e);
    }

    return null;
  }

  @Override
  public PostResponse getBlogPost(String postId) {

    final String getPostSql = "SELECT * FROM blog_posts WHERE id = ?";
    final String getAuthorInfoSql = "SELECT first_name, last_name FROM users WHERE cid = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement getPostPstmt = conn.prepareStatement(getPostSql);
            PreparedStatement getAuthorInfoPstmt = conn.prepareStatement(getAuthorInfoSql)) {

      try {

        conn.setAutoCommit(false);

        getPostPstmt.setString(1, postId);

        ResultSet getPostRset = getPostPstmt.executeQuery();

        if (getPostRset.next()) {

          PostResponse post = new PostResponse();
          post.setId(getPostRset.getString("id"));
          post.setTitle(getPostRset.getString("title"));
          post.setContent(getPostRset.getString("content"));
          post.setMainImage(getPostRset.getString("main_image"));
          post.setVisible(getPostRset.getBoolean("is_visible"));
          post.setCid(getPostRset.getString("cid"));

          getAuthorInfoPstmt.setString(1, post.getCid());

          ResultSet getAuthorInfoRset = getAuthorInfoPstmt.executeQuery();

          if (getAuthorInfoRset.next()) {
            Names names = Names.builder().firstName(getAuthorInfoRset.getString("first_name")).lastName(getAuthorInfoRset.getString("last_name")).build();
            post.setNames(names);
          }

          post.setCreatedAt(getPostRset.getTimestamp("created_at"));
          post.setUpdatedAt(getPostRset.getTimestamp("updated_at"));

          return post;
        }

      } catch (SQLException ex) {
        log.error("Error getting post with ID: '" + postId + "'", ex);
//        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error getting post with ID: '" + postId + "'", e);
    }

    return null;
  }

  @Override
  public PostResponse editBlogPost(PostEditModel pem) {

    final String updatePostSql = "UPDATE blog_posts SET title = ?, content = ?, updated_at = NOW() WHERE id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement updatePostPstmt = conn.prepareStatement(updatePostSql)) {

      try {

        conn.setAutoCommit(false);

        updatePostPstmt.setString(1, pem.getTitle());
        updatePostPstmt.setString(2, pem.getContent());
        updatePostPstmt.setString(3, pem.getId());

        updatePostPstmt.executeUpdate();

        conn.commit();

        return getBlogPost(pem.getId());

      } catch (SQLException ex) {
        log.error("Error updating post with ID: '" + pem.getId() + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error updating post with ID: '" + pem.getId() + "'", e);
    }

    return null;
  }

  @Override
  public boolean changeBlogPostVisibility(String postId, boolean isVisible) {

    final String changePostVisibilitySql = "UPDATE blog_posts SET is_visible = ? WHERE id = ?";

    try (Connection conn = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            PreparedStatement changePostVisibilityPstmt = conn.prepareStatement(changePostVisibilitySql)) {

      try {

        conn.setAutoCommit(false);

        changePostVisibilityPstmt.setBoolean(1, isVisible);
        changePostVisibilityPstmt.setString(2, postId);
        changePostVisibilityPstmt.executeUpdate();

        conn.commit();

        return true;

      } catch (SQLException ex) {
        log.error("Error changing blog post visibility with ID: '" + postId + "'", ex);
        conn.rollback();
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (Exception e) {
      log.error("Error changing blog post visibility with ID: '" + postId + "'", e);
    }

    return false;
  }
}

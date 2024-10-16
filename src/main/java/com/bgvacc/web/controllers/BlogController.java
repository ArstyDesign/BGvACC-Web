package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.responses.blog.PostResponse;
import com.bgvacc.web.services.BlogService;
import com.bgvacc.web.utils.Breadcrumb;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class BlogController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final BlogService blogService;

  @GetMapping("/blog/posts")
  public String getBlogPosts(Model model) {

    List<PostResponse> posts = blogService.getVisibleBlogPosts();

    model.addAttribute("posts", posts);

    model.addAttribute("pageTitle", getMessage("blog.posts.title"));
    model.addAttribute("page", "blog");
    model.addAttribute("subpage", "posts");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.blog", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.blog.posts", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "blog/posts";
  }

  @GetMapping("/blog/posts/{postId}")
  public String getBlogPost(@PathVariable("postId") String postId, Model model) {

    PostResponse post = blogService.getBlogPost(postId);

    model.addAttribute("post", post);

    model.addAttribute("pageTitle", getMessage("blog.posts.title"));
    model.addAttribute("page", "blog");
    model.addAttribute("subpage", "posts");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("menu.home", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.blog", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("menu.blog.posts", null, LocaleContextHolder.getLocale()), "/blog/posts"));
    breadcrumbs.add(new Breadcrumb(post.getTitle(), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "blog/post";
  }

  @PostMapping("/portal/blog/posts/{postId}/show")
  public String showPost(@PathVariable("postId") String postId, Model model) {

    log.debug("Showing post");

    boolean isChanged = blogService.changeBlogPostVisibility(postId, true);

    return "redirect:/portal/blog/posts";
  }

  @PostMapping("/portal/blog/posts/{postId}/hide")
  public String hidePost(@PathVariable("postId") String postId, Model model) {

    log.debug("Hiding post");

    boolean isChanged = blogService.changeBlogPostVisibility(postId, false);

    return "redirect:/portal/blog/posts";
  }
}

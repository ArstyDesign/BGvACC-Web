package com.bgvacc.web.controllers.portal;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.models.portal.blog.posts.PostEditModel;
import com.bgvacc.web.responses.blog.PostResponse;
import com.bgvacc.web.services.BlogService;
import com.bgvacc.web.utils.Breadcrumb;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
@RequiredArgsConstructor
public class PortalBlogController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final BlogService blogService;

  @GetMapping("/portal/blog/posts")
  public String getPosts(Model model) {

    List<PostResponse> posts = blogService.getBlogPosts();

    model.addAttribute("posts", posts);

    model.addAttribute("pageTitle", getMessage("portal.blog.posts.title"));
    model.addAttribute("page", "blog");
    model.addAttribute("subpage", "posts");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.blog.title", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.blog.posts", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/blog/posts/posts";
  }

  @GetMapping("/portal/blog/posts/{postId}/edit")
  public String preparePostEdit(@PathVariable("postId") String postId, Model model) {

    PostResponse post = blogService.getBlogPost(postId);

    PostEditModel pem = new PostEditModel();
    pem.setId(postId);
    pem.setTitle(post.getTitle());
    pem.setContent(post.getContent());
    pem.setVisible(post.isVisible());

    model.addAttribute("pem", pem);

    model.addAttribute("pageTitle", getMessage("portal.blog.posts.edit.title"));
    model.addAttribute("page", "blog");
    model.addAttribute("subpage", "posts");

    List<Breadcrumb> breadcrumbs = new ArrayList<>();
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.blog.title", null, LocaleContextHolder.getLocale()), null));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.blog.posts", null, LocaleContextHolder.getLocale()), "/portal/blog/posts"));
    breadcrumbs.add(new Breadcrumb(getMessage("portal.blog.posts.edit.title", null, LocaleContextHolder.getLocale()), null));

    model.addAttribute("breadcrumbs", breadcrumbs);

    return "portal/blog/posts/postEdit";
  }

  @PostMapping("/portal/blog/posts/{postId}/edit")
  public String editPost(@PathVariable("postId") String postId, @Valid @ModelAttribute("pem") PostEditModel pem, BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors()) {
      
      log.debug("Form has erros: " + bindingResult.toString());

      model.addAttribute("pageTitle", getMessage("portal.blog.posts.edit.title"));
      model.addAttribute("page", "blog");
      model.addAttribute("subpage", "posts");

      List<Breadcrumb> breadcrumbs = new ArrayList<>();
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu", null, LocaleContextHolder.getLocale()), "/portal/dashboard"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.blog.title", null, LocaleContextHolder.getLocale()), null));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.menu.blog.posts", null, LocaleContextHolder.getLocale()), "/portal/blog/posts"));
      breadcrumbs.add(new Breadcrumb(getMessage("portal.blog.posts.edit.title", null, LocaleContextHolder.getLocale()), null));

      model.addAttribute("breadcrumbs", breadcrumbs);

      return "portal/blog/posts/postEdit";
    }

    log.debug("Updating blog post");
    PostResponse updatedPost = blogService.editBlogPost(pem);

    return "redirect:/portal/blog/posts";
  }
}

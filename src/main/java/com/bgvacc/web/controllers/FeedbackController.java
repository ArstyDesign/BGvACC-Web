package com.bgvacc.web.controllers;

import com.bgvacc.web.base.Base;
import com.bgvacc.web.models.FeedbackReportModel;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
@Controller
public class FeedbackController extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @GetMapping("/feedback-report")
  public String prepareFeedbackReport(Model model) {

    model.addAttribute("pageTitle", "Feedback report");

    model.addAttribute("frm", new FeedbackReportModel());

    model.addAttribute("page", "feedback-report");

    return "feedback-report";
  }

  @PostMapping("/feedback-report")
  public String sendFeedbackReport(@ModelAttribute("frm") @Valid FeedbackReportModel frm,
          BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest request, Model model) {

    log.debug("Testing feedback report: " + frm);

    if (bindingResult.hasErrors()) {

      model.addAttribute("pageTitle", "Feedback report");

      model.addAttribute("page", "feedback-report");

      return "feedback-report";
    }

    return "redirect:/feedback-report";
  }
}

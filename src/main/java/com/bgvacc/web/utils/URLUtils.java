package com.bgvacc.web.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class URLUtils {

  private final Logger log = LoggerFactory.getLogger(getClass());

  public String getURLAsHtmlLink(String url) {
    return getURLAsHtmlLink(url, false);
  }

  public String getURLAsHtmlLink(String url, boolean shouldOpenNewPage) {

    StringBuilder builder = new StringBuilder();
    builder.append("<a href=\"").append(url).append("\" class=\"text-primary\"");

    if (shouldOpenNewPage) {
      builder.append(" target=\"_blank\"");
    }

    builder.append(">").append(url).append("</a>");

    return String.valueOf(builder);
  }

  public String transformUrlToHtmlLinkInText(String text, boolean shouldOpenNewPage) {

    String result = "";

    List<String> replacedUrls = new ArrayList<>();

    for (String url : findUrlInText(text)) {
      String urlAsHtmlLink = getURLAsHtmlLink(url, shouldOpenNewPage);

      if (!replacedUrls.contains(url)) {
        result = text.replace(url, urlAsHtmlLink);
        replacedUrls.add(url);
      }
    }

    return result;
  }

  public List<String> findUrlInText(String text) {

    Pattern pattern = Pattern.compile("(https?://)(www\\.)?([\\w-]+)\\.(\\w+)(/[^\\s]*)?");
    Matcher matcher = pattern.matcher(text);

    List<String> list = new ArrayList<>();

    while (matcher.find()) {
      list.add(matcher.group());
    }

    return list;
  }
}

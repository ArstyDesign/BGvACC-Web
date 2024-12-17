package com.bgvacc.web.beans;

import com.bgvacc.web.base.Base;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Atanas Yordanov Arshinkov
 * @since 1.0.0
 */
public class Version extends Base {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private ServletContext context;

  private String CURRENT_VERSION = null;

  public synchronized String getVersion() {
    if (CURRENT_VERSION != null) {
//      CURRENT_VERSION = getMessage("footer.progress");
      CURRENT_VERSION = getMessage("footer.beta");
    }

    try {
      if (context != null) {
        InputStream is = context.getResourceAsStream("/META-INF/MANIFEST.MF");
        Properties props = new Properties();
        props.load(is);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date from = sdf.parse(props.getProperty("Implementation-Date"));

        SimpleDateFormat fromFormat = new SimpleDateFormat("dd.MM.yyyy");

        CURRENT_VERSION = "v" + props.get("Implementation-Version") + " - " + fromFormat.format(from);
      }
    } catch (Exception e) {
//      CURRENT_VERSION = getMessage("footer.progress");
      CURRENT_VERSION = getMessage("footer.beta");
//      CURRENT_VERSION = "Work in progress...";
    }

    return CURRENT_VERSION;
  }
}

package cn.exhibition.admin.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringUtilsTest {

    @Test
    void summary_stripsTagsEntitiesAndWhitespace() {
        assertThat(StringUtils.summary("<p>迪拜&nbsp;能源展</p>\n\n<p>  &lt;报名中&gt;  </p>")).isEqualTo("迪拜 能源展 <报名中>");
    }

    @Test
    void summary_emptyForBlankOrImageOnly() {
        assertThat(StringUtils.summary(null)).isEmpty();
        assertThat(StringUtils.summary("  ")).isEmpty();
        assertThat(StringUtils.summary("<p><img src=\"a.png\"></p>")).isEmpty();
    }

    @Test
    void summary_cutsToLimit() {
        String text = "展".repeat(StringUtils.SUMMARY_LENGTH + 30);
        assertThat(StringUtils.summary("<p>" + text + "</p>")).hasSize(StringUtils.SUMMARY_LENGTH);
    }
}

package org.needlehack.collector.infrastructure.crawl;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.needlehack.collector.infrastructure.crawl.dto.CrawlSection;
import org.needlehack.collector.infrastructure.crawl.dto.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class PostCrawler implements ICrawler {

    protected static String USER_AGENT =
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.89 Safari/537.36";

    private static final Logger log = LoggerFactory.getLogger(PostCrawler.class);

    @Override
    public CrawlSection crawl(final String link) {
        Post post = this.extractPostData(link);
        return new CrawlSection(post.getContent(), post.getTags());
    }

    protected Post extractPostData(final String urlPageFrom) {
        Post post = retrievePostByUrl(urlPageFrom);
        if (post != null) {
            cleanHTML(post);
            disambiguate(post);
        }
        return post;
    }

    /**
     * Clean HTML of all private fields of T Post with @Whitelist basic of @Jsoup elements.
     *
     * @param post
     */
    protected void cleanHTML(final Post post) {
        try {
            BeanInfo bi = Introspector.getBeanInfo(post.getClass());
            final List<PropertyDescriptor> propDes = Arrays.asList(bi.getPropertyDescriptors());
            final Object[] input = null;
            for (PropertyDescriptor prop : propDes) {
                final Method readMethod = prop.getReadMethod();
                if (readMethod != null && readMethod.getReturnType()
                        .isAssignableFrom(String.class)) {
                    final String value = (String) readMethod.invoke(post, input);
                    if (value != null && value.trim()
                            .length() > 0) {
                        String valueC = Jsoup.clean(value, Whitelist.basic());
                        valueC = StringEscapeUtils.unescapeHtml4(valueC);
                        final Method writeMethod = prop.getWriteMethod();
                        writeMethod.invoke(post, valueC);
                    }
                }
            }
        } catch (IntrospectionException | IllegalArgumentException | IllegalAccessException
                | InvocationTargetException e) {

        }
    }

    protected void disambiguate(final Post post) {

    }

    /**
     * Retrieve the basic version of a post associated to url param.
     *
     * @param urlPageFrom URL page.
     * @return The basic version of a site.
     */
    protected abstract Post retrievePostByUrl(final String urlPageFrom);

    public Post retrievePost(String urlPageFrom, final String selectorContent,
                             final String selectorCategories) {

        Post.PostBuilder postBuilder = new Post.PostBuilder();

        try {
            Document doc = JsoupHelper.getJsoupDocument(urlPageFrom, Charset.forName("UTF-8"), USER_AGENT);
            doc.outputSettings()
                    .escapeMode(EscapeMode.xhtml);


            postBuilder.withContent(getContentToPost(doc, selectorContent));
            postBuilder.withTags(getTagsToPost(doc, selectorCategories));
        } catch (Exception e) {
            log.error("method:{}.{}|cause:\'{}\'|message:\'{}\'|exception:\'{}\'|extra:\'{}\'",
                    "Extractor", "retrievePost", e.getCause() != null ? e.getCause() : "NULL",
                    e.getMessage(), e, urlPageFrom);
        }
        return postBuilder.createPost();
    }

    protected String getContentToPost(final Document docHtml,
                                      final String selectorContent) {
        // 1- CONTENT
        Elements elementsContent = docHtml.select(selectorContent);
        String contentPost =
                (elementsContent != null && !elementsContent.isEmpty()) ? elementsContent.text() : null;
        return contentPost;
    }

    protected Set<String> getTagsToPost(final Document docHtml,
                                        final String selectorCategories) {

        // 2- TAGS
        final Set<String> tagsPost = new HashSet<>();
        Elements elementsTags = StringUtils.isEmpty(selectorCategories) ? new Elements(0) : docHtml.select(selectorCategories);
        elementsTags.forEach(marker -> tagsPost.add(marker.text()));
        return tagsPost;
    }
}

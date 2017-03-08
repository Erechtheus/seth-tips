package de.dfki.nlp.loader;

import com.google.common.collect.Lists;
import de.dfki.nlp.config.AnnotatorConfig;
import de.dfki.nlp.domain.IdList;
import de.dfki.nlp.domain.ParsedInputText;
import de.dfki.nlp.io.RetryHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AbstractServerFetcher extends AbstractDocumentFetcher {

    private final AnnotatorConfig annotatorConfig;
    private final RetryHandler retryHandler;

    public AbstractServerFetcher(AnnotatorConfig annotatorConfig, RetryHandler retryHandler) {
        this.annotatorConfig = annotatorConfig;
        this.retryHandler = retryHandler;
    }


    @Override
    List<ParsedInputText> load(IdList idList) {

        try {

            Optional<ParsedInputText[]> parsedInputText = retryHandler.retryablePost(annotatorConfig.abstractserver.url, new MultiRequest(idList.getIds()), ParsedInputText[].class);

            if (parsedInputText.isPresent()) {
                return Arrays.stream(parsedInputText.get()).map(m -> {
                    // move text to abstract text
                    m.setAbstractText(m.getText());
                    m.setText(null);
                    return m;
                }).collect(Collectors.toList());
            }

            return Lists.newArrayList();


        } catch (RestClientException e) {
            log.error("Error retrieving abstracts {}", idList, e);
        }

        return null;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class MultiRequest {
        List<String> abstracts;
    }
}
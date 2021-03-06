package de.dfki.nlp.loader;

import com.google.common.collect.Lists;
import de.dfki.nlp.domain.IdList;
import de.dfki.nlp.domain.ParsedInputText;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MultiDocumentFetcher extends AbstractDocumentFetcher {

    @Autowired
    PubMedDocumentFetcher pubMedDocumentFetcher;

    @Autowired
    PatentServerFetcher patentServerHandler;

    @Autowired
    PMCDocumentFetcher pmcDocumentFetcher;

    @Autowired
    AbstractServerFetcher abstractServerHandler;

    private static final List<ParsedInputText> emptyResultList = Lists.newArrayList(new ParsedInputText());

    @Override
    public List<ParsedInputText> load(IdList idList) {

        List<ParsedInputText> parsedInputTexts = null;

        switch (idList.getSource().toLowerCase(Locale.ENGLISH)) {
            case "pubmed":
                parsedInputTexts = pubMedDocumentFetcher.load(idList);
                break;

            case "patent server":
                parsedInputTexts = patentServerHandler.load(idList);
                break;

            case "pmc":
                parsedInputTexts = pmcDocumentFetcher.load(idList);
                break;

            case "abstract server":
                parsedInputTexts = abstractServerHandler.load(idList);
                break;

            case "inline":
                parsedInputTexts = idList
                        .getDocuments()
                        .stream().map(d -> new ParsedInputText(d.getDocument_id(), d.getTitle(), d.getAbstractText(), d.getText()))
                        .collect(Collectors.toList());
                break;

            default:
                log.error("Don't know how to handle: {}", idList.getSource());
        }

        if (parsedInputTexts == null || parsedInputTexts.size() == 0) {
            return emptyResultList;
        }
        return parsedInputTexts;

    }

}

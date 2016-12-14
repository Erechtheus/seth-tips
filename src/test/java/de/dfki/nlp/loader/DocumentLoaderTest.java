package de.dfki.nlp.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.dfki.nlp.config.GeneralConfig;
import de.dfki.nlp.domain.ParsedInputText;
import de.dfki.nlp.domain.rest.ServerRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {GeneralConfig.class, DocumentLoader.class, ObjectMapper.class} )
public class DocumentLoaderTest {

    @Autowired
    DocumentLoader documentLoader;

    @Test
    public void testNotImplementedLoader() throws Exception {

        ParsedInputText load = documentLoader.load(new ServerRequest.Document("BC1403855C", "PMC"));

        assertThat(load.getDocumentID()).isNull();
        assertThat(load.getTitleText()).isNull();
        assertThat(load.getAbstractText()).isNull();

    }

    @Test
    public void testPubMed() throws Exception {
        // now test pubmed

        ParsedInputText load = documentLoader.load(new ServerRequest.Document("BC1403854C", "pubmed"));

        assertThat(load.getDocumentID()).isEqualTo("BC1403854C");
        assertThat(load.getTitleText()).isEqualTo("Twelve-year clinical report on multiple endodontic implant stabilizers.");
        assertThat(load.getAbstractText()).isNull();

    }
}
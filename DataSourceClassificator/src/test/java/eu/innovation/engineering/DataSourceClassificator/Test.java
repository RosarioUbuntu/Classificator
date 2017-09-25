package eu.innovation.engineering.DataSourceClassificator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;

import eu.innovation.engineering.LSA.keywordExtractor.LSACosineKeywordExtraction;
import eu.innovation.engineering.LSA.keywordExtractor.LSAKeywordExtractor;
import eu.innovation.engineering.config.PathConfigurator;
import eu.innovation.engineering.keyword.extractor.innen.InnenExtractor;
import eu.innovation.engineering.keyword.extractor.interfaces.KeywordExtractor;
import eu.innovation.engineering.util.preprocessing.Paper;
import eu.innovation.engineering.util.preprocessing.SolrClient;
import eu.innovation.engineering.util.preprocessing.Source;

public class Test {



  public static void main(String[] args) throws Exception{
    /*
    System.out.println(DatasetBuilder.loadSources(PathConfigurator.applicationFileFolder+"sources.json").size());
    System.out.println(SourceVectorBuilder.loadSourceVectorList(PathConfigurator.applicationFileFolder+"sourceVectors.json").size());
    System.out.println(DatasetBuilder.loadSources(PathConfigurator.applicationFileFolder+"dictionariesCategory/science/trainingScience.json").size());
     */
    KeywordExtractor lsaKe = new LSAKeywordExtractor(PathConfigurator.keywordExtractorsFolder);
    KeywordExtractor innKe = new InnenExtractor(PathConfigurator.keywordExtractorsFolder);
    LSACosineKeywordExtraction lsaCosKe = new LSACosineKeywordExtraction(PathConfigurator.keywordExtractorsFolder);


    ObjectMapper mapper = new ObjectMapper();
    Map<String,List<String>> glossaryMap = mapper.readValue(new File(PathConfigurator.applicationFileFolder+"Glossaries.json"),new TypeReference<Map<String,List<String>>>() {});

    List<String> toCompare = new ArrayList<>();
    for(String glossary: glossaryMap.keySet()){
      toCompare.addAll(glossaryMap.get(glossary));
    }



    SolrClient solrClient = new SolrClient();
    String id="10008302_234";
    String id2 ="10018323_234";
    String id3 = "10013570_234";

    

    List<String> ids = new ArrayList<>();
    ids.add(id);
    ids.add(id3);
    ids.add(id2);
    List<Source> sources = solrClient.getSourcesFromSolr(ids, Paper.class);

    for(Source source: sources){
      String description = source.getTitle()+" "+source.getTexts().get(1);
      String fullText = source.getTexts().get(2);

      List<String> toAnalyze = new ArrayList<>();

      toAnalyze.add(description);
      //toAnalyze.add(fullText);


      List<List<Keyword>> innResults = innKe.extractKeywordsFromTexts(toAnalyze, 5);
      //    List<List<Keyword>> lsaResults = lsaKe.extractKeywordsFromTexts(toAnalyze, 5);
      List<List<Keyword>> lsaCosResults = lsaCosKe.extractKeywordsFromTexts(toAnalyze, toCompare, 5);

      System.out.println("ID -> "+source.getId());
      System.out.println("Title ->"+source.getTitle()+"\n");

      System.out.println("Keyword from description");
      System.out.println("INNEN ->"+innResults.get(0));
      //    System.out.println("LSA ->"+lsaResults.get(0));
      System.out.println("LSACosine -> "+lsaCosResults.get(0));
      System.out.println("--------------------------\n");
    }
    /*
    System.out.println("Keyword from full_text");
    System.out.println("INNEN ->"+innResults.get(1));
//    System.out.println("LSA ->"+lsaResults.get(1));
    System.out.println("LSACosine -> "+lsaCosResults.get(1));
    System.out.println("--------------------------\n");
     */
  }
}

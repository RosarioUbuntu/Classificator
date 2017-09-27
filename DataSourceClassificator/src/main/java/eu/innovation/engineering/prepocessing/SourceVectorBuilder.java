package eu.innovation.engineering.prepocessing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.innovation.engineering.LSA.keywordExtractor.LSACosineKeywordExtraction;
import eu.innovation.engineering.config.PathConfigurator;
import eu.innovation.engineering.keyword.extractor.interfaces.KeywordExtractor;
import eu.innovation.engineering.prepocessing.datareader.TxtDataReader;
import eu.innovation.engineering.prepocessing.featurextractor.ClusteringKMeans;
import eu.innovation.engineering.util.featurextractor.SourceVector;
import eu.innovation.engineering.util.preprocessing.Source;

/**
 * @author Rosario
 *
 */
public class SourceVectorBuilder {

  
  
  public static void main(String[] args) throws Exception{
    boolean fromSolr = true;
    boolean withCategories = false;
    SourceVectorBuilder sourceVectorBuilder = new SourceVectorBuilder();
    sourceVectorBuilder.buildSourceVectors(PathConfigurator.rootFolder+"science/","science",withCategories,fromSolr);
  }
  
  
  /**
   * @param args
   * @throws Exception 
   * @throws s 
   */
  public void buildSourceVectors (String path,String category,boolean withCategories,boolean fromSolr) throws Exception{

    String trainingFile = path+"training.txt";
    String glossaryFile = path+"glossaries.json";
    String pathWhereSave = path+"sourceVector.json";
    
    //prendo gli id dal file training.txt
    TxtDataReader dataReader = new TxtDataReader();
    dataReader.setFileToReadSource(trainingFile);    
    List<String> sourcesIds = new ArrayList<>();
    sourcesIds.addAll(dataReader.getIds());
    System.out.println("Sources size "+sourcesIds.size());
    List<Source> sources = new ArrayList<>();
    DatasetBuilder dataBuilder = new DatasetBuilder();
    if(fromSolr){   
      KeywordExtractor ke = new LSACosineKeywordExtraction(PathConfigurator.keywordExtractorsFolder, glossaryFile);
      dataBuilder.setKeywordExtractor(ke);
      if(!withCategories){    
        sources = dataBuilder.buildDataset("training.txt", path, "categories.txt", false);
      }else      
        sources = dataBuilder.buildDataset("training.txt", path, "../categories.txt", true);
      DatasetBuilder.saveSources(sources, path+"sources.json");
    }else{
      sources = DatasetBuilder.loadSources(path+"training.json");
    }
    pathWhereSave = path+"sourceVectors.json";
    saveSourceVectorList(pathWhereSave, createSourceVectorList(sources,category));
  }
  
  /**
   * @param List<Source> sources
   * @return List<SourceVector> 
   * @throws IOException
   */
  public List<SourceVector> createSourceVectorList(List<Source> sources,String category) throws IOException{
    List<SourceVector> toReturn = new ArrayList<>();   
    ClusteringKMeans clustering = new ClusteringKMeans();
    float[][] vectors = clustering.returnVectorsFromSourceList((ArrayList<Source>) sources);    
    for(int i=0;i<sources.size();i++){
      SourceVector sv = new SourceVector();
      sv.setId(sources.get(i).getId());
      sv.setCategory(category);
      sv.setTitle(sources.get(i).getTitle());
      sv.setKeywords(sources.get(i).getKeywordList());
      sv.setVector(vectors[i]);
      toReturn.add(sv);
    }
    vectors = null;
    return toReturn;
  }

  /**
   * @param path (.json)
   * @param List<Source> 
   * @throws JsonGenerationException
   * @throws JsonMappingException
   * @throws IOException
   */
  public static void saveSourceVectorList(String path,List<SourceVector> list) throws JsonGenerationException, JsonMappingException, IOException{
    ObjectMapper mapper = new ObjectMapper();
    mapper.writerWithDefaultPrettyPrinter().writeValue(new File(path), list);
    System.out.println("Source vectors saved into "+path);
  }

  /**
   * Load Function for Source Vectors from a json file
   * @param path (.json)
   * @return List<SourceVector>
   * @throws JsonParseException
   * @throws JsonMappingException
   * @throws IOException
   */
  public static List<SourceVector> loadSourceVectorList(String path) throws JsonParseException, JsonMappingException, IOException{
    ObjectMapper mapper = new ObjectMapper();
    List<SourceVector> loadedList = mapper.readValue(new File(path), new TypeReference<List<SourceVector>>() {});
    System.out.println("Source vectors loaded from "+path);
    return loadedList;
  }
}

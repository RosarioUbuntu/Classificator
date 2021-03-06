package eu.innovation.engineering.prepocessing.featurextractor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer.EmptyClusterStrategy;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.cxf.jaxrs.client.WebClient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.ibm.watson.developer_cloud.alchemy.v1.model.Keyword;

import eu.innovation.engineering.config.PathConfigurator;
import eu.innovation.engineering.prepocessing.DatasetBuilder;
import eu.innovation.engineering.prepocessing.DictionaryBuilder;
import eu.innovation.engineering.prepocessing.SourceVectorBuilder;
import eu.innovation.engineering.util.featurextractor.Item;
import eu.innovation.engineering.util.featurextractor.ItemWrapper;
import eu.innovation.engineering.util.featurextractor.SourceVector;
import eu.innovation.engineering.util.preprocessing.CosineDistance;
import eu.innovation.engineering.util.preprocessing.Source;
import eu.innovationengineering.word2vec.common.Constants;
import eu.innovationengineering.word2vec.common.request.bean.VectorListRequestBean;
import eu.innovationengineering.word2vec.service.rest.impl.Word2vecServiceImpl;



public class ClusteringKMeans {

  public static void main (String args[]) throws IOException{

    //clusterWithDatasourceAsItems(PathConfigurator.trainingAndTestFolder+"trainingBig.json",500);
    // clusterWithDatasourceAsItems(PathConfigurator.trainingAndTestFolder+"dataSourcesWithoutCategory_10000_10000.json",3000);
    //clusterSubCategory(PathConfigurator.applicationFileFolder+"sourceVectors.json",PathConfigurator.categories+"scienceJson.json", "science");
  }



  // Misura l'accuratezza del clustering con l'indice di DAVIS-Bouldin
  public static float DaviesBouldinIndex(List<CentroidCluster<ItemWrapper>> clusterResults, int cut){

    float DB = 0; 



    for(int i=0;i<cut;i++){
      CentroidCluster<ItemWrapper> pointsI = clusterResults.get(i);
      //Calcolo la distanza media del cluster I
      ClusteringKMeans clustering = new ClusteringKMeans();
      float avgI = clustering.avgDistanceIntraCluster(clusterResults.get(i));
      float max = -32000;
      for(int j=0;j<cut;j++){
        if(i!=j){
          float avgJ = clustering.avgDistanceIntraCluster(clusterResults.get(j));
          double centroidDistance = Math.acos(FeatureExtractor.cosineSimilarity(pointsI.getCenter().getPoint(), clusterResults.get(j).getCenter().getPoint()));
          //System.out.println("i: "+i+" j: "+j+" avgI: "+avgI+" avgJ: "+avgJ+" centroidDistance: "+centroidDistance);
          if(max < ((avgI+avgJ)/centroidDistance)){
            max = (float) ((avgI+avgJ)/centroidDistance);
          }
        }
      }
      DB+=max;
    }


    return DB/cut;

  }


  public  float avgDistanceIntraCluster(CentroidCluster<ItemWrapper> cluster){
    float avg = 0;
    Clusterable center = cluster.getCenter();
    for(ItemWrapper point : cluster.getPoints()){
      avg+=Math.acos(FeatureExtractor.cosineSimilarity(point.getPoint(), center.getPoint()));
    }
    return avg=avg/cluster.getPoints().size();



  }

  public  void clusterWithKeywordsAsItems(String fileName, int cut) throws IOException {
    CreateMatrix matrixCreator = new CreateMatrix();

    DatasetBuilder pb = new DatasetBuilder();
    pb.parseDatasetFromJson(fileName);

    ArrayList<Source> paperList = pb.getSourceList();
    HashSet<String> keywordList = pb.returnAllKeywords(paperList);

    HashMap<String, ArrayList<Double>> matrixKeywordsDocument = matrixCreator.getMatrixKeywordsDocuments(paperList,keywordList);

    ArrayList<Item> items = new ArrayList<Item>();



    for(String k : keywordList){
      Item item = new Item();
      item.setId(k);
      item.setFeatures(matrixKeywordsDocument.get(k).stream().mapToDouble(Double::doubleValue).toArray());   
      items.add(item);
    }


    // we have a list of our locations we want to cluster. create a     
    List<ItemWrapper> clusterInput = new ArrayList<ItemWrapper>(items.size());

    for(Item i : items){
      clusterInput.add(new ItemWrapper(i));
    }

    // lo metto a null per liberare memoria
    items=null;


    KMeansPlusPlusClusterer<ItemWrapper> clusterer = new KMeansPlusPlusClusterer<ItemWrapper>(cut, -1, new CosineDistance(), new JDKRandomGenerator(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);

    List<CentroidCluster<ItemWrapper>> clusterResults = clusterer.cluster(clusterInput);




    FileWriter write = new FileWriter("Clusters.txt");
    // output the clusters
    for (int i=0; i<clusterResults.size(); i++) {
      System.out.println("Cluster " + i);
      write.write("Cluster " + i+"\n");
      for (ItemWrapper itemWrapper : clusterResults.get(i).getPoints()){
        System.out.println(itemWrapper.getItem().getId());
        write.write(itemWrapper.getItem().getId()+"\n");
      }
      write.write("\n\n");

    }

    write.flush();
    write.close();



  }


  public  void clusterSubCategory(String sourceFile, String categoryFile, String categoryChoose) throws JsonParseException, JsonMappingException, IOException{

    ObjectMapper mapper = new ObjectMapper();
    HashMap<String,float[]> categoryVectorList = mapper.readValue(new File(categoryFile), new TypeReference<HashMap<String,float[]>>() {});

    List<SourceVector> sourceList = SourceVectorBuilder.loadSourceVectorList(sourceFile);

    PrintWriter writer = new PrintWriter(PathConfigurator.applicationFileFolder+"CosineSimilarityResults.txt");
    ArrayList<Item> items = new ArrayList<Item>(); 

    for(SourceVector source : sourceList){
      if(source.getCategory().contains(categoryChoose)){
        writer.println("\nID: "+source.getId());
        writer.println("TITLE: "+source.getTitle());
        writer.println("KEYWORDS: "+source.getKeywords().toString());
        Item item = new Item();
        item.setId(source.getId());
        item.setDatasource("Paper");
        item.setTitle(source.getTitle()+"\n"+source.getKeywords().toString()+"\n");
        double[] features = new double[categoryVectorList.size()];
        int count = 0;
        for(String category : categoryVectorList.keySet()){

          features[count] = FeatureExtractor.cosineSimilarity(source.getVector(), categoryVectorList.get(category));
          writer.println("      "+category+": "+features[count]);
          count++;
        }
        item.setFeatures(features);
        items.add(item);
      }

    }
    writer.flush();
    writer.close();




    List<ItemWrapper> clusterInput = items.stream().map(ItemWrapper::new).collect(Collectors.toList());
    KMeansPlusPlusClusterer<ItemWrapper> clusterer = new KMeansPlusPlusClusterer<ItemWrapper>(4);

    System.out.println("Number datasource to create dictionaries: "+clusterInput.size()+" num Cluster:"+4);
    System.out.println("Starting k-means");
    List<CentroidCluster<ItemWrapper>> clusterResults = clusterer.cluster(clusterInput);
    System.out.println("Ended k-means");

    System.out.println("DaviesBouldin-Index: "+DaviesBouldinIndex(clusterResults,4));

    writer = new PrintWriter(PathConfigurator.applicationFileFolder+"clusters.txt");
    for (int i=0; i<clusterResults.size(); i++) {
      writer.println("\nCluster: "+i);
      System.out.println("\n\nCluster: "+i);
      for (ItemWrapper itemWrapper : clusterResults.get(i).getPoints()){
        writer.println("    id: "+itemWrapper.getItem().getId()+"   Keywords: "+itemWrapper.getItem().getTitle());
        System.out.println("    id: "+itemWrapper.getItem().getId()+"   Keywords: "+itemWrapper.getItem().getTitle());
      }
    }
    writer.flush();
    writer.close();
  }







  public  HashMap<String, Dictionary> clusterWithDatasourceAsItems(String fileName, int cut, String path) throws IOException {
    CreateMatrix matrixCreator = new CreateMatrix();


    DatasetBuilder dataBuilder = new DatasetBuilder();
    dataBuilder.parseDatasetFromJson(fileName);
    // PRENDO LA LISTA DI PAPER DAL FILE USANDO IL METODO DELL OGGETTO pb
    ArrayList<Source> paperList = dataBuilder.getSourceList();

    //INIZIALIZZO UNA LISTA DI ITEMS, CHE SARANNO GLI OGGETTI CHE VERRANNO CLUSTERIZZATI
    ArrayList<Item> items = new ArrayList<Item>();

    //CI PRENDIAMO I VETTORI PER OGNI PAPER
    float resultsVector[][] = returnVectorsFromSourceList(paperList);

    ToDoubleFunction<float[]> norm = v -> {
      double normSquared = 0.0;
      for (int i = 0; i < v.length; i++) {
        normSquared += v[i] * v[i];
      }
      return Math.sqrt(normSquared);
    };

    for(int i=0; i<resultsVector.length;i++){
      if (norm.applyAsDouble(resultsVector[i]) == 0.0) {
        System.out.println("Skipping zero-length vector for id " + paperList.get(i).getId());
        continue;
      }
      Item item = new Item();
      item.setId(paperList.get(i).getId());

      double results[] = new double[resultsVector[i].length];
      for (int j = 0; j < resultsVector[i].length; j++) {
        results[j] = resultsVector[i][j];
      }
      //AGGIUNGO ALL'ITEM IL VETTORE COME FEATURE 
      item.setFeatures(results);
      //item.setFeatures(matrixDocuementKeywords.get(p.getId()).stream().mapToDouble(Double::doubleValue).toArray());   
      item.setDatasource("Paper");
      items.add(item);
    }


    // we have a list of our locations we want to cluster. create a     
    List<ItemWrapper> clusterInput = items.stream().map(ItemWrapper::new).collect(Collectors.toList());

    // initialize a new clustering algorithm. 
    // we use KMeans++ with 10 clusters and 10000 iterations maximum.
    // we did not specify a distance measure; the default (euclidean distance) is used.
    KMeansPlusPlusClusterer<ItemWrapper> clusterer = new KMeansPlusPlusClusterer<ItemWrapper>(cut, -1, new CosineDistance(), new JDKRandomGenerator(), EmptyClusterStrategy.LARGEST_POINTS_NUMBER);

    System.out.println("Number datasource to create dictionaries: "+clusterInput.size()+" num Cluster:"+cut);
    System.out.println("Starting k-means");
    List<CentroidCluster<ItemWrapper>> clusterResults = clusterer.cluster(clusterInput);
    System.out.println("Ended k-means");

    System.out.println("DaviesBouldin-Index: "+DaviesBouldinIndex(clusterResults,cut));

    //creo la lista di dizionari, ogni dizionario contiene la lista di keywords e il vettore che rappresenta l'intero dizionario, utile nell'analisi LSA
    HashMap<String,Dictionary> dictionaries = new HashMap<>();

    //ciclo sui cluster ottenuti, per ogni cluster creo un dizionario che contiene tutte le keywords dei paper che appartengono al cluster
    for (int i=0; i<clusterResults.size(); i++) {
      HashMap<String,Double> keywords = new HashMap<>();
      for (ItemWrapper itemWrapper : clusterResults.get(i).getPoints()){
        String id = itemWrapper.getItem().getId();
        for(Source p : paperList){
          if(p.getId().equals(id))
            for(Keyword k : p.getKeywordList()){
              if(keywords.keySet().contains(k.getText()))
                keywords.put(k.getText(), keywords.get(k.getText())+k.getRelevance());
              else
                keywords.put(k.getText(), k.getRelevance());
            }
        }
      }
      Dictionary dictionary= new Dictionary();
      dictionary.setKeywords(keywords);
      dictionaries.put("Cluster "+i, dictionary);
    }

    // CALCOLO MEDIA E VARIANZA INTRACLUSTER (INTERNA AL DIZIONARIO) SCARTANDO QUELLI CON VALORE PIU' BASSO

    for(String key : dictionaries.keySet()){
      Dictionary d = dictionaries.get(key);
      d.setAvg(avg(d));
      d.setVariance(variance(d,d.getAvg()));
      List<String> keywordToRemove = new ArrayList<String>(); 
      //CREO LA LISTA DI KEYWORD DA RIMUOVERE


      for(String keyMap: d.getKeywords().keySet()){
        if(d.getKeywords().get(keyMap) < (d.getAvg() -(d.getVariance()/2))){
          keywordToRemove.add(keyMap);
        }
      }


      for(String keys: keywordToRemove){
        d.getKeywords().remove(keys);
      }
    }


    //STAMPO SU FILE I CLUSTER OTTENUTI
    FileWriter writer = new FileWriter(PathConfigurator.dictionariesFolder+cut+"_dictionaries.txt");

    for(String cluster : dictionaries.keySet()){
      writer.write(cluster+"\n");
      Dictionary currentDictionary = dictionaries.get(cluster);

      for(String keymap : currentDictionary.getKeywords().keySet()){
        writer.write("    "+keymap+" -> "+currentDictionary.getKeywords().get(keymap)+"\n");
      }
      writer.write("AVG: "+currentDictionary.getAvg()+"\n");
      writer.write("Variance: "+currentDictionary.getVariance()+"\n");
      writer.write("\n\n");
    }

    writer.flush();
    writer.close();


    // per ogni dizionario calcolo anche i vettori che mi serviranno successivamente. 
    HashMap<String, Dictionary> finalDictionaries = returnVectorForDictionaries(dictionaries);
    DictionaryBuilder db = new DictionaryBuilder();
    db.save(finalDictionaries, path+"dictionaries.json");

    return finalDictionaries;

  }




  private  float variance(Dictionary d, float avg) {
    float sum = 0;

    for(String keymap : d.getKeywords().keySet()){
      sum+=(d.getKeywords().get(keymap) - avg)*(d.getKeywords().get(keymap) - avg);
    }
    return (float) Math.sqrt(sum/(d.getKeywords().size()));
  }




  private  float avg(Dictionary d) {

    float sum = 0;
    for(String keymap : d.getKeywords().keySet()){
      sum+=d.getKeywords().get(keymap);
    }
    return sum/d.getKeywords().size();
  }




  private  HashMap<String, Dictionary> returnVectorForDictionaries(HashMap<String, Dictionary> dictionaries) throws IOException {

    List<List<String>> docsK = new ArrayList<List<String>>();

    // pr ogni dizionario
    for(String cluster: dictionaries.keySet()){
      // creo l'arrayList di stringhe da passare a word2Vec
      ArrayList<String> stringToVec = new ArrayList<>();

      for(String keymap : dictionaries.get(cluster).getKeywords().keySet()){
        String parts[] = keymap.split(" ");
        Arrays.stream(parts).forEach(stringToVec::add);
      }
      docsK.add(stringToVec);
    }
    VectorListRequestBean vectorListRequest = new VectorListRequestBean();
    vectorListRequest.setDocs(docsK);

    // faccio le richieste a word2Vec per i vettori
    WebClient webClient = WebClient.create("http://smartculture-projects.innovationengineering.eu/word2vec-rest-service/", Arrays.asList(new JacksonJaxbJsonProvider()));

    try (Word2vecServiceImpl word2vecService = new Word2vecServiceImpl()) {      
      word2vecService.setWebClient(webClient);
      float[][] vectorListKeyword = word2vecService.getVectorList(Constants.GENERAL_CORPUS, Constants.ENGLISH_LANG, vectorListRequest);
      int iteratorDictionary=0;
      for(String dictionary : dictionaries.keySet()){
        double[] app = new double[vectorListKeyword[iteratorDictionary].length];
        for(int i = 0; i<vectorListKeyword[iteratorDictionary].length;i++){
          app[i]= vectorListKeyword[iteratorDictionary][i];
        }
        // converto il vettore double in un vettore float
        float [] floatApp = new float[app.length];
        for(int i=0;i<app.length;i++){
          floatApp[i]=(float) app[i];
        }
        //aggiungo al dizionario il vettore ottenuto da word2vec
        dictionaries.get(dictionary).setVector(floatApp);
        iteratorDictionary++;
      }

    }

    return dictionaries;

  }


  public  float[][] returnVectorsFromTextList(ArrayList<List<String>> textList) throws IOException{


    VectorListRequestBean vectorListRequest = new VectorListRequestBean();
    vectorListRequest.setDocs(textList);
    //chiamo il wordToVec per calcolare il vettore delle stinghe ottenute
    WebClient webClient = WebClient.create("http://smartculture-projects.innovationengineering.eu/word2vec-rest-service/", Arrays.asList(new JacksonJaxbJsonProvider()));

    try (Word2vecServiceImpl word2vecService = new Word2vecServiceImpl()) {      
      word2vecService.setWebClient(webClient);
      return word2vecService.getVectorList(Constants.GENERAL_CORPUS, Constants.ENGLISH_LANG, vectorListRequest);
    }
    catch(Exception e){
      System.out.println(e);
      return null;
    }

  }

  public  float[][] returnVectorsFromSourceList(ArrayList<Source> sourceList) throws IOException {

    //ISTANZIAMO UNA MATRICE DI STRINGHE
    List<List<String>> docsK = new ArrayList<List<String>>();

    //PER OGNI PAPER COSTRUIAMO IL SET DI KEYWORDS DA CUI POI OTTENERE IL VETTORE
    for(Source p: sourceList){
      //calcolo la relevance minima
      double minRelevance = p.getKeywordList().stream().mapToDouble(Keyword::getRelevance).min().getAsDouble();

      // creo l'arrayList di stringhe da passare a word2Vec
      ArrayList<String> stringToVec = new ArrayList<>();

      for(Keyword k : p.getKeywordList()){
        double resultDivision = 0;
        if(minRelevance>0){
          resultDivision = k.getRelevance()/minRelevance;
        }
       // if(resultDivision>10)
          //resultDivision=10;
        int numOccurence = (int) Math.ceil(resultDivision);
        //Aggiungo la keyword nel vettore di keywords "numOccurence" volte
        String parts[] = k.getText().replace("-", " ").split(" ");
          for(int i = 0; i<numOccurence; i++){
            //Arrays.stream(parts).forEach(stringToVec::add);
            for(int j=0;j<parts.length;++j){
              stringToVec.add(parts[j]);
            }
          }
      }
      //AGGIUNGO LA LISTA DI KEYWORDS ALLA MATRICE PER IL PAPER CORRENTE
      docsK.add(stringToVec);
    }
    VectorListRequestBean vectorListRequest = new VectorListRequestBean();
    vectorListRequest.setDocs(docsK);

    //chiamo il wordToVec per calcolare il vettore delle stinghe ottenute

    WebClient webClient = WebClient.create("http://smartculture-projects.innovationengineering.eu/word2vec-rest-service/", Arrays.asList(new JacksonJaxbJsonProvider()));

    try (Word2vecServiceImpl word2vecService = new Word2vecServiceImpl()) {      
      word2vecService.setWebClient(webClient);
      float[][] vectorListKeyword = word2vecService.getVectorList(Constants.GENERAL_CORPUS, Constants.ENGLISH_LANG, vectorListRequest);
      int vectorLength = Arrays.stream(vectorListKeyword).mapToInt(v -> v.length).max().getAsInt();
      for (int i = 0; i < vectorListKeyword.length; i++) {
        if (vectorListKeyword[i].length != vectorLength) {
          float newVector[] = new float[vectorLength];
          System.arraycopy(vectorListKeyword[i], 0, newVector, 0, vectorListKeyword[i].length);
          vectorListKeyword[i] = newVector;
        }
      }
      return vectorListKeyword ;

    }


  }


}

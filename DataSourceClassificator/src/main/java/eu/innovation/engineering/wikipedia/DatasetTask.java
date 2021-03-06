package eu.innovation.engineering.wikipedia;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.RecursiveTask;

public class DatasetTask extends RecursiveTask<Map<String,Set<DocumentInfo>>> implements Callable<Map<String,Set<DocumentInfo>>> {

  private String category;
  private int maxLevel;
  private Map<String, Map<String, CategoryInfo>> map;
  private boolean withRootCategories;
  private boolean recursive;


  public DatasetTask(String category,Map<String, Map<String, CategoryInfo>> map,int maxLevel,boolean withRootCategories,boolean recursive){
    this.category = category;
    this.maxLevel = maxLevel;
    this.map = map;
    this.withRootCategories = withRootCategories;
    this.recursive = recursive;
  }


  @Override
  public Map<String, Set<DocumentInfo>> call() throws Exception {
    return compute();
  }

  @Override
  protected Map<String, Set<DocumentInfo>> compute() {
    Map<String, Set<DocumentInfo>> toReturn = new HashMap<>();
    try {
      Set<String> listIdDocuments = WikipediaMiner.requestIdsPagesOfCategory(category, new HashSet<String>(), recursive, 0, maxLevel);
      Map<String, DocumentInfo> contents = WikipediaMiner.getContentPages(listIdDocuments);

      Set<DocumentInfo> listDocument = new HashSet<>();
      for(String idDoc: contents.keySet()){
        DocumentInfo docInfo = contents.get(idDoc);
        if(withRootCategories)          
          docInfo.setRootCategories(WikipediaMiner.getRootBelongCategories(map, WikipediaMiner.getBelongCategories(idDoc)));
        listDocument.add(docInfo);
      }
      toReturn.put(category, listDocument);
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    System.out.println(category+" -> done");
    return toReturn;
  }



}

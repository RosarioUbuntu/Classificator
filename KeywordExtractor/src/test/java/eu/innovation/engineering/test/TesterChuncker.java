package eu.innovation.engineering.test;

import java.util.ArrayList;
import java.util.List;

import eu.innovation.engineering.LSA.keywordExtractor.LSACosineKeywordExtraction;
import eu.innovation.engineering.LSA.keywordExtractor.LSAKeywordExtractor;
import eu.innovation.engineering.keyword.extractor.innen.InnenExtractor;
import eu.innovation.engineering.keyword.extractor.interfaces.KeywordExtractor;

public class TesterChuncker {

  public static void main(String[] args) throws Exception{


    String test = "Development of the BASS rake acoustic current sensor : measuring velocity in the continental shelf wave bottom boundary layer"
        +"Surface swell over the continental shelf generates a sheet of oscillatory shear flow at"
        +"the base of the water column, the continental shelf wave bottom boundary layer. The"
        +"short periods of surface swell sharply limit the thickness of the wave boundary layer,"
        +"confining it to a thin region below an oscillatory, but essentially irrotational, core. For"
        +"a wide range of shelf conditions, the vertical extent of the wave boundary layer does"
        +"not exceed 2.5 cm and is commonly less. The extreme narrowness of this boundary"
        +"layer is responsible for high levels of bottom stress and turbulent dissipation. Even in"
        +"relatively mild sea states, the wave induced bottom shear stress can be sufficient to"
        +"initiate sediment motion. The wave bottom boundary layer plays an important role"
        +"in the processes of sediment entrainment and transport on the continental margins."
        +"This thesis documents the development, testing, and field use of a new instrument,"
        +"the BASS Rake, designed to measure velocity profiles in the wave boundary layer."
        +"The mechanical design supports multiple measurement levels with millimeter vertical"
        +"spacing. The mechanical design is integrated with an electronic interface designed to"
        +"permit flexible acquisition of a suite of horizontal and vertical velocity measurements"
        +"without sacrificing the electronic characteristics necessary for high measurement accuracy. The effects of velocity averaging over the sample volume are calculated with"
        +"a model of acoustic propagation in a scattering medium appropriate to the scales of"
        +"a single differential travel time axis. A simpler parametric model of the averaging"
        +"process is then developed and used to specify the transducer characteristics necessary"
        +"to image the wave boundary layer on the continental shelf."
        +"A flow distortion model for the sensor head is developed and the empirical determinations"
        +"of the Reynolds number, Keulegan-Carpenter number, and angular dependencies"
        +"of the sensor response for the laboratory and field prototypes is presented."
        +"The calibrated sensor response of the laboratory prototype is tested against concurrent"
        +"LDV measurements over a natural sand bed in a flume. The single measurement"
        +"accuracy of the BASS Rake is higher than that of the LDV and the multiple sample volumes confer other advantages. For example, the ability of the BASS Rake to image"
        +"vertically coherent turbulent instabilities, invisible to the LDV, is demonstrated."
        +"Selected data from a twenty-four day field deployment outside the surf zone of a local"
        +"beach are presented and analyzed. The data reveal regular reworking of the sand"
        +"bed, the generation and modification of sand ripples, and strong tidal modulation of"
        +"the current and wave velocities on semi-diurnal, diurnal, and spring eap time scales."
        +"The data set is unique in containing concurrent velocity time series, of several weeks"
        +"duration, with coverage from 1 cm to 20 cm above the bottom.Submitted in partial fulfillment of the requirements for the degree of Doctor of Philosophy at the Massachusetts Institute of Technology and the Woods Hole Oceanographic Institution June 1997ONR has contributed"
        +"substantial financial support to this research and to my graduate education"
        +"through AASERT funding under ONR Grant N00014-93-1-1140. NSF supported the purchase of"
        +"hardware and services under NSF Grant OCE-9314357";

    String test2 = "The umbilical region, in the anatomists' abdominal pelvic nine-region scheme, is the area surrounding the umbilicus (navel). This region of the abdomen contains part of the stomach, the" 
        +"head of the pancreas, the duodenum, a section of the transverse colon and the lower aspects of the left and right kidney. The upper three regions, from left to right, are the left "
        +"hypochondriac, epigastric, and right hypochondriac regions. The middle three regions, from left to right, are the left lumbar, umbilical, and right lumbar regions. The bottom three" 
        +"regions, from left to right, are the left inguinal, hypogastric, and right inguinal regions.";

    String test3 = "Solar energy is becoming increasingly attractive as we grapple with global climate changes. However, while solar energy is free, non-polluting, and inexhaustible, solar panels are fixed. As such, "
        + "they cannot take advantage of maximum sunlight as weather conditions and seasons change. A solar panel receives the most sunlight when it is perpendicular to the sun’s rays, but the sunlight direction changes regularly with changing seasons and weather. Currently, most solar panels are fixed, i.e., the solar array has a fixed orientation to the sky and does not turn to follow the sun. To increase the unit area illumination of sunlight on solar panels, we designed a solar tracking electricity generation system. The design mechanism holds the solar panel and allows the panel to perform an approximate3-dimensional (3-D) hemispheroidal rotation to track the sun’s movement during the day and improve the overall electricity generation. This system can achieve the maximum illumination and energy concentration and cut the cost of electricity by requiring fewer solar panels, therefore, it has great significance for research and development. The main use of this report is to utilize the maximum power from the sun. Now a day we are in heavy need to use the solar power as in the coming days everything we use might depend on this kind of systems";

    String test4 = "The effects of change in fuel density, change in clad material and change in fuel material on the inherent safety features of a typical material test reactor were analyzed. The International Atomic Energy Agency’s 10 MW benchmark reactor was selected for the study. Standard computer codes WIMS-D4 and CITATION were used to perform neutronics calculations while PARET was used to carryout the steady state and transient thermal hydraulic analysis. In all, seven thermal hydraulic simulations were performed for each configuration. They were the steady state analysis, four controlled transients i.e. fast reactivity insertion, slow reactivity insertion, fast loss of flow and slow loss of flow transients, and two uncontrolled reactivity insertion transients, i.e. small reactivity insertion and large reactivity insertion transients.\r\nTwo families of the high density dispersion fuels were analyzed to see the effect of changed uranium density on the inherent safety features of the reactor. These families were U3Si2-Al (having uranium densities of 4.10, 4.80 and 5.66 g/cm3) on the lower side and U9Mo-Al (having uranium densities of 6.57, 7.74 and 8.90 g/cm3) on the upper side. It was observed that the steady state thermodynamic behaviour of all the fuels was same, only the fuel temperatures of U3Si2-Al fuels showed some differences. During the fast reactivity insertion transient, the maximum reactor power achieved increased by about 29% for U3Si2 fuel-family while the increase was 45% for U9Mo fuel-family. This resulted in increased maximum temperatures of fuel, clad and coolant outlet, achieved during the transient. This increase for U3Si2 fuels was 32 K, 21.1 K and 5.1 K respectively, while for U9Mo fuels it was 27.7 K, 19.7 K and 7.9 K respectively for maximum fuel, clad and coolant outlet temperatures. During the slow reactivity insertion and loss of flow transients, no appreciable difference in the reactor power and temperature profiles was observed. For small reactivity insertion transient, the new power level increased as uranium density increased. The increase was 8.1% for U3Si2 fuel-family while it was 5.8% for U9Mo fuel-family. In uncontrolled large reactivity insertion transient, the feedback reactivities were unable to control the reactor which resulted in the coolant boiling; the one with the highest fuel density was the first to reach the ONB.\r\nIn order to see the effects of different fuel materials, the original aluminide (UAlx-Al) fuel of the reactor was replaced with silicide (U3Si-Al and U3Si2-Al) and oxide (U3O8-Al) dispersion fuels having the same uranium density of 4.40 g/cm3 as of the original fuel. The oxide fuel had higher fuel temperatures during steady state and transients. During fast reactivity insertion transient, the maximum power reached for oxide fuel was 0.35 MW lesser than that of aluminide fuel, but its maximum fuel temperature was 13 K higher. With respect to the UAlx-Al fuel, the maximum powers of U3Si-Al and U3Si2-Al fuels were higher by 2.11 MW and 1.82 MW respectively, while the maximum fuel temperatures were lower by 5.7 K and 4.5 K respectively. During slow reactivity insertion and loss of flow transients, the power and temperature profiles of all the fuels were almost the same only fuel temperatures varying; the maximum fuel temperature of the oxide fuel being 8 K to 12 K higher than that of the other fuels. During uncontrolled small reactivity insertion transient, the maximum fuel temperature attained by the oxide fuel was almost 16 K higher than that of the others at the new steady state. During uncontrolled large reactivity insertion transient, the coolant of oxide fuel was the last to reach the ONB but again at the cost of higher fuel temperature.\r\nIn order to see the effects of different clad materials, only the Al clad and side plates of the reactor fuel were replaced by stainless steel (clad of a fast reactor) and zircaloy-4 (clad of a PWR). The zircaloy-4 clad gave a positive clad temperature feedback coefficient. The very high absorption cross section of stainless steel made it a very unlikely choice for clad material. Out of the remaining two, the main difference was in the fuel temperatures with zircaloy-4 cladded fuel having higher fuel temperatures. The temperature of zircaloy-4 cladded fuel was 20 K to 40 K higher than that of Al cladded fuel during different transients.";

    String test5 = "Enel SpA (Enel) is a multinational energy company and a global integrated operator in the electricity and gas industries with a focus on Europe and Latin America. The Company\'s segments include Italy, Iberian Peninsula, Latin America, Eastern Europe, Renewable Energy and Other. The Company\'s divisions include Generation, Trading, Infrastructure and Networks, Upstream Gas and Renewable Energy. Its global reach extends from Europe, to North America, Latin America, Africa and Asia. The Company operates in approximately 30 countries on over four continents with a net installed capacity of approximately 90 gigawatt (GW). The distribution companies transport electricity through a network of over 1.9 million kilometers total. The Company has power generation plants of all types in approximately 10 countries from Alberta in Canada to the central Andes, and supply energy to cities in South America, including Rio de Janeiro, Bogota, Buenos Aires, Santiago de Chile and Lima.";

    String test6 = "We are a simple successful Pan European Commercial Bank, with a fully plugged in CIB, delivering a unique Western, Central and Eastern European network to our extensive client franchise: 25 million clients."+
        "We  offer local expertise as well as international reach. We accompany and support our 25 million clients globally, providing them with unparalleled access to our leading banks in 14 core markets as well as to an another 18 countries worldwide."+
        "Our European banking network includes Italy, Germany, Austria, Bosnia and Herzegovina, Bulgaria, Croatia, Czech Republic, Hungary, Romania, Russia, Slovakia, Slovenia, Serbia and Turkey."+
        "Our strategic position in Western and Central and Eastern Europe enables us to command one of highest market shares in the region."+
        "While our brand is recognizable all over Europe, we have preserved the highly valuable local brands of banks that we acquired to form our Group.";
    
    String test7 = "Irresistible Vegan Strawberry Cheesecake Bites BY: LAUREN TOYOTA AND JOHN DIEMER FEBRUARY 10, 2018 | 7:30 AM I’m over chocolate on Valentine’s Day, which is why we created this sultry two-bite dessert you can whip up for yourself, your sweetheart, or all your single gal pals! They’re vegan, raw and gluten-free, giving you just the energy boost you need for some lone time — or, staying up all night gawking at Tinder.";
    
    String test8 ="The Poli Grappa Museum is set up in Bassano del Grappa, the capital of Grappa, inside a palace of the fifteenth century, in front of the historic wooden Bridge 'Ponte Vecchio'.The history of distillation and of Grappa is explained by means of a brief but detailed educational tour. The Museum consists of three little rooms: in the first hall, the reconstructed stills and collection of interesting documents will reveal the mysteries of the origin and the evolution of the art of distillation over the years.In the second hall, by means of pictures, stills and apparatus for making Grappa, it will be possible to appreciate and understand the reason why this Italian distillate is unique in the world. The objects' descriptions and the images' captions are in Italian and in English.In the third hall there is a show-room where it is possible to taste the products of the Jacopo Poli Distillery.";
    
    String test9 = "Haute cuisine developed out of political and social changes in France. The 'high' cuisine represented a hierarchy in 17th century France as only the privileged could eat it. Haute cuisine distinguished itself from regular French cuisine by what was cooked and served such as foods like tongue and caviar, by serving foods such as fruit out of season, by making it difficult and time consuming to cook, and by using exotic ingredients not typically found in France. [1]In addition to who was eating haute cuisine and what exactly it consisted of, the term can also be defined by who was making it and how they were doing so. Professionally trained chefs were quintessential to the birth of haute cuisine in France. The extravagant presentations and complex techniques that these chefs were known for required ingredients, time, equipment, and therefore money. For this reason, early haute cuisine was accessible to a small demographic of rich and powerful individuals. Professional French chefs were not only responsible for building and shaping haute cuisine, but their roles in the cuisine were what differentiated it from regular French cuisine.[2]Haute cuisine was characterized by French cuisine in elaborate preparations and presentations served in small and numerous courses that were produced by large and hierarchical staffs at the grand restaurants and hotels of Europe. The cuisine was very rich and opulent with decadent sauces made out of butter, cream, and flour, the basis for many typical French sauces that are still used today. [3] The 17th century chef and writer La Varenne marked a change from cookery known in the Middle Ages, to somewhat lighter dishes, and more modest presentations. In the following century, Antonin Carême, also published works on cooking, and although many of his preparations today seem extravagant, he simplified and codified an earlier and even more complex cuisine.";
    
    
    String test10 = "An access policy with respect to a data object is defined by using a data structure (270) that comprises at least one interest-based access list (271) and at least one privacy-based access list (272).            An interest-based access list (271) specifies a metadata and a group of persons that are potentially interested in accessing a data object that can be characterized by this metadata.            A privacy-based access list (272) specifies a metadata and a group of persons that are authorized to access a data object that can be characterized by this metadata.            The data object of interest is analyzed (210) so as to obtain at least one metadata that characterizes the data object.            A search is carried out (251) in the data structure (270) so as to identify at least one interest-based access list and at least one privacy-based access list specifying metadata that matches with a metadata that characterizes the data object.            A recommended access list is composed (252) that specifies those persons who occur in at least one interest-based access list that has been identified, as well as in at least one privacy-based access list that has been identified.";
    
    String test11 = "There is a growing need for ad-hoc analysis of extremely large data sets, especially at internet companies where innovation critically depends on being able to analyze terabytes of data collected every day. Parallel database products, e.g., Teradata, offer a solution, but are usually prohibitively expensive at this scale. Besides, many of the people who analyze this data are entrenched procedural programmers, who find the declarative, SQL style to be unnatural. The success of the more procedural map-reduce programming model, and its associated scalable implementations on commodity hardware, is evidence of the above. However, the map-reduce paradigm is too low-level and rigid, and leads to a great deal of custom user code that is hard to maintain, and reuse. We describe a new language called Pig Latin that we have designed to fit in a sweet spot between the declarative style of SQL, and the low-level, procedural style of map-reduce. The accompanying system, Pig, is fully implemented, and compiles Pig Latin into physical plans that are executed over Hadoop, an open-source, map-reduce implementation. We give a few examples of how engineers at Yahoo! are using Pig to dramatically reduce the time required for the development and execution of their data analysis tasks, compared to using Hadoop directly. We also report on a novel debugging environment that comes integrated with Pig, that can lead to even higher productivity gains. Pig is an open-source, Apache-incubator project, and available for general use";
    
    LSACosineKeywordExtraction kex = new LSACosineKeywordExtraction("","data/glossaries/food.json");


    List<String> testList = new ArrayList<>();
    testList.add(test10 );
    System.out.println("LSA Cosine Version:\n"+kex.extractKeywordsFromTexts(testList, 5));


    /*
    List<String> toCompare = new ArrayList<>();

    toCompare.addAll(kex.readGlossay("data/Glossary_of_biology.txt"));
    toCompare.addAll(kex.readGlossay("data/Glossary_of_chemistry_terms.txt"));
    toCompare.addAll(kex.readGlossay("data/mathematics.txt"));

    //toCompare.add("science");
    System.out.println(kex.extractKeywordsFromTexts(testList, toCompare,5));
     */

    long startTime = 0;
    KeywordExtractor ke = new InnenExtractor("");
    KeywordExtractor lsake = new LSAKeywordExtractor("");

    startTime = System.currentTimeMillis();
    System.out.println("Innen Extractor:\n"+ke.extractKeywordsFromTexts(testList, 4));


    startTime = System.currentTimeMillis();
    System.out.println("LSA Paper Version:\n"+lsake.extractKeywordsFromTexts(testList, 4));



    /*
    StanfordnlpAnalyzer analyzer = new StanfordnlpAnalyzer();
    StanfordLemmatizer lemmStan = new StanfordLemmatizer();
    Lemmatizer lemm = new Lemmatizer();
    long startTime = 0;





    startTime = System.currentTimeMillis();
    System.out.println(kex.createSentencesFromText(test));



    startTime = System.currentTimeMillis();
    lemm.lemmatize(test);
    System.out.println(System.currentTimeMillis() - startTime);

    /*
    startTime = System.currentTimeMillis(); 
    List<String> sentences = analyzer.detectSentences(test, ISO_639_1_LanguageCode.ENGLISH);
    System.out.println(System.currentTimeMillis() - startTime);

    startTime = System.currentTimeMillis();    
    System.out.println(lemmStan.lemmatizeTerm(test, "", ISO_639_1_LanguageCode.ENGLISH));
    System.out.println(System.currentTimeMillis() - startTime);
     */

  }




}

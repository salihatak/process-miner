package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import main.processminig.domain.Item;

public class Main {

	static String PREVLIST = "PREVLIST";
	static String NEXTLIST = "NEXTLIST";
		
	Map<String, List<String>> cases = new HashMap<String, List<String>>();
	Map<String, List<String>> variants = new HashMap<String, List<String>>();
	Map<String, Integer> activities = new HashMap<String, Integer>();
	List<String> activitiesList = new ArrayList<String>();
	Map<String, Map<String, Map<String, String>>> footprintMap = new HashMap<String, Map<String,Map<String, String>>>();
 
	public static void main(String[] args) throws IOException, ParseException {
//		Main main = new Main();
//		main.readLogs(true);
//		
//		System.out.println("------ Cases ------");
//		for(String key : main.cases.keySet())
//			System.out.println(key);
//
//		System.out.println("------ Variants ------");
//		for(String key : main.variants.keySet())
//			System.out.println(key + " - " + main.variants.get(key));
//
//		System.out.println("------ Activities ------");
//		for(String key : main.activitiesList)
//			System.out.println(key + " - " + main.activities.get(key));
//
//		System.out.println("------ VExport Activities ------");
//		for(String key : main.activitiesList)
//			System.out.println("{ data: { id: '" + key + "' } },");
//		
//		main.createFootPrints();

		UUID idOne = UUID.randomUUID();
	    System.out.println("UUID One: " + idOne);
		
	}

	void createFootPrints(){
		/** -- find footprint -- */
		for(String key : variants.keySet()){
			List<String> activitiesOfVariant = variants.get(key);
			for(int i = 0 ; i < activitiesOfVariant.size(); i++){
				String activity = activitiesOfVariant.get(i);
				
				// -- put prev footprint of current
				Map<String, Map<String, String>> footprint = footprintMap.get(activity);
				if(footprint == null)
					footprint = new HashMap<String, Map<String, String>>();
				
				Map<String, String> fpMap = footprint.get(PREVLIST);
				if(fpMap == null)
					fpMap = new HashMap<String, String>();
				
				String prev = null;
				if(i > 0){
					prev = activitiesOfVariant.get(i - 1);
					fpMap.put(prev, "-");
				}
				footprint.put(PREVLIST, fpMap);
				footprintMap.put(activity, footprint);
				// -- put current next footprint of prev
				if(prev != null){
					footprint = footprintMap.get(prev);
					if(footprint == null)
						footprint = new HashMap<String, Map<String, String>>();
					
					fpMap = footprint.get(NEXTLIST);
					if(fpMap == null)
						fpMap = new HashMap<String, String>();
					
					fpMap.put(activity, "-");
					footprint.put(NEXTLIST, fpMap);
					footprintMap.put(prev, footprint);
				}
			}
		}
		/** ---------------- */
		
		System.out.println("------ Footprints ------");
		System.out.print("  ");
		for(int i = 0 ; i < activitiesList.size() ; i++)
			System.out.print(activitiesList.get(i) + "\t");
		System.out.println();
		
		for(int i = 0 ; i < activitiesList.size() ; i++){
			System.out.print(activitiesList.get(i) + "\t");
			for(int j = 0 ; j < activitiesList.size() ; j++ ){
				Map<String, Map<String, String>> fpAktivity = footprintMap.get(activitiesList.get(i));
				
				if(fpAktivity != null){
					Map<String, String> nextMap = fpAktivity.get(NEXTLIST);
					Map<String, String> prevMap = fpAktivity.get(PREVLIST);
					if(nextMap != null && nextMap.get(activitiesList.get(j)) != null){
						if(prevMap != null && prevMap.get(activitiesList.get(j)) != null)
							System.out.print("=" + "\t");
						else
							System.out.print(">" + "\t");
					} else if(prevMap != null && prevMap.get(activitiesList.get(j)) != null){
						if(nextMap != null && nextMap.get(activitiesList.get(j)) != null)
							System.out.print("=" + "\t");
						else
							System.out.print("<" + "\t");
					} else
						System.out.print("#" + "\t");
				} else
					System.out.print("#" + "\t");
			}
			System.out.println();
		}
		
		/** -- find causality relation -- */
		System.out.println("------ causality relation ------");
		for(int i = 0 ; i < activitiesList.size() ; i++){
			String activity = activitiesList.get(i);
			Map<String, Map<String, String>> fpAktivity = footprintMap.get(activity);
			Map<String, String> nextMap = fpAktivity.get(NEXTLIST);
//			Map<String, String> prevMap = fpAktivity.get(PREVLIST);
			/** delete parallel steps */ 
//			removeIntersections(prevMap, nextMap);
			/** --------------------- */ 
			if(nextMap != null && nextMap.size() > 0){
				String buffer = "";
				if(nextMap.size() == 1){
					buffer = nextMap.keySet().iterator().next();
				} else {
					buffer = "(";
					for(String target : nextMap.keySet()){
						buffer += target + ",";
					}
					buffer = buffer.substring(0, buffer.length() - 1);
					buffer += ")";
				}
				System.out.print("(" + activity + "," + buffer + "),");
			}
		}
		System.out.println();
		/** ---------------- */

		/** -- find direct relation -- */
		System.out.println("------ VExport direct relation ------");
		for(int i = 0 ; i < activitiesList.size() ; i++){
			String activity = activitiesList.get(i);
			Map<String, Map<String, String>> fpAktivity = footprintMap.get(activity);
			Map<String, String> nextMap = fpAktivity.get(NEXTLIST);
			if(nextMap != null && nextMap.size() > 0){
				for(String target : nextMap.keySet()){
					System.out.println("{ data: { id: '"+activity + target+"', weight: 1, source: '"+activity+"', target: '"+target+"' } },");
				}
			}
		}
		/** ---------------- */
		
		
	}
	
	@SuppressWarnings("rawtypes")
	void removeIntersections(Map mapA, Map mapB){
		if(mapA != null)
			for (Iterator iterator = mapA.keySet().iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
			    if (mapB != null && mapB.containsKey(key)) {
			    	iterator.remove();
//			        mapA.remove(key);
			        mapB.remove(key);
			    }
			}
//			for (Object key : mapA.keySet().iterator()) {
//			    if (mapB != null && mapB.containsKey(key)) {
//			        mapA.remove(key);
//			        mapB.remove(key);
//			    }
//			}
	}
	
	void readLogs(boolean withDate) throws IOException, ParseException{
//		BufferedReader in = new BufferedReader(new FileReader("D:/demo_log_1.csv"));
//		BufferedReader in = new BufferedReader(new FileReader("D:/demo_log.log"));
//		BufferedReader in = new BufferedReader(new FileReader("D:/demo_log_2.log"));
//		BufferedReader in = new BufferedReader(new FileReader("D:/demo_log_3.log"));
		BufferedReader in = new BufferedReader(new FileReader("D:/demo_log_4.log"));
		String line = "";
		boolean ignoreHeaderLine = true;
		
		if(withDate){
			SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			List<Item> logList = new ArrayList<Item>();
			while((line = in.readLine()) != null){
				if(!ignoreHeaderLine){
					String[] cols = line.split(";");
					logList.add(new Item(cols[0], cols[1], df.parse(cols[2])));
				}
				if(ignoreHeaderLine)
					ignoreHeaderLine = false;
			}
			
			logList.sort(new Comparator<Item>() {
				public int compare(Item o1, Item o2) {
					if(o1.getDate().after(o2.getDate()))
						return 1;
					else if(o2.getDate().after(o1.getDate()))
						return -1;
					else
						return 0;
				}
			});

			for(Item item : logList){
				List<String> list = cases.get(item.getCaseId());
				
				/** -- find cases -- */
				if(list == null)
					list = new ArrayList<String>();
				list.add(item.getActivity());
				cases.put(item.getCaseId(), list);
				/** ---------------- */

				/** -- find activities -- */
				Integer activityCount = activities.get(item.getActivity());
				if(activityCount == null){
					activityCount = 1;
					activitiesList.add(item.getActivity());
				}else
					activityCount++;
				activities.put(item.getActivity(), activityCount);
				/** ---------------- */
			}
			
		} else { /** Without date sorted log */
			while((line = in.readLine()) != null){
				if(!ignoreHeaderLine){
					String[] cols = line.split(";");
					List<String> list = cases.get(cols[0]);
					
					/** -- find cases -- */
					if(list == null)
						list = new ArrayList<String>();
					list.add(cols[1]);
					cases.put(cols[0], list);
					/** ---------------- */

					/** -- find activities -- */
					Integer activityCount = activities.get(cols[1]);
					if(activityCount == null){
						activityCount = 1;
						activitiesList.add(cols[1]);
					}else
						activityCount++;
					activities.put(cols[1], activityCount);
					/** ---------------- */
				}
				
				if(ignoreHeaderLine)
					ignoreHeaderLine = false;
			}
		}
		
		for(Map.Entry<String, List<String>> entry :  cases.entrySet()){
			String variant = "";
			
			List<String> activitiesOfCase = entry.getValue();
			
			/** -- find variants -- */
			for(String activity : activitiesOfCase){
				variant += activity + ";";
			}
			/** ---------------- */
			
//			Integer variantCount = variants.get(variant);
//			if(variantCount == null)
//				variantCount = 1;
//			else
//				variantCount++;
			
			variants.put(variant, entry.getValue());
		}
		
		in.close();
	}
}

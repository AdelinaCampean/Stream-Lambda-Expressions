package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import MD.MonitoredData;

import static java.util.stream.Collectors.toList;
import java.util.*; 
import java.util.stream.Collectors; 

public class Main {


	public static long data (String a, String b ) {
		DateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //stackoverflow
		 Date start;
		 Date end;
		 long fin = 0;
		try {
			start = form.parse(b);
			end = form.parse(a);
			fin = end.getTime() - start.getTime();
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 
		 return fin;
	}
	
	
	
	public static String data2(String a, String b ) {
		DateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //stackoverflow
		 Date start;
		 Date end;
		 long fin = 0;
		 long dif = 0;
		 String time="";
		try {
			start = form.parse(b);
			end = form.parse(a);
			fin = end.getTime() - start.getTime();
			
			dif =fin/1000;
			time = String.valueOf(dif/3600);
			time+=" Hours ";
			time+= String.valueOf((dif%3600)/60);
			time+=" MIN ";
			time+= String.valueOf((dif%60));
			time+= " Seconds ";
			
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		 
		 return time;
	}
	
	public static void main(String[] args) {

			String fName = "Activities.txt";
			
					List<MonitoredData> mo = new ArrayList<MonitoredData>();
					
				try (Stream<String> str = Files.lines(Paths.get(fName))) {
					
					str
					  .forEach((line) -> {
							
							MonitoredData mon = new MonitoredData();
							mon.setStart_time(line.substring(0, 19));
							mon.setEnd_time(line.substring(21, 40));
							mon.setActivity(line.substring(42));
							mo.add(mon);
							
								
						});
					str.close();
					
					System.out.println("CERINTA 1\n"+"Lista de activitati: \n");
					for (MonitoredData t: mo)
						System.out.println(t.toString());
			}
			catch (IOException e) {
				e.printStackTrace();
			}
				
				
				///CERINTA 2
				System.out.println("\n");
				System.out.println("CERINTA 2\n");

						long a = mo.stream()

							.filter(k-> k.getEnd_time().substring(8,10).compareTo(k.getStart_time().substring(8,10))!=0)
							.count();
					System.out.println("Activitatile se desfasoara pe parcursul a "+ ++a +" zile\n");
				
				
					//CERINTA 3
					System.out.println("\n");
					System.out.println("CERINTA 3\n");
					
				Map<String, Long> obj = mo.stream() 
						//.sorted(Comparator.comparingLong(g-> Integer.parseInt(g.getStart_time().substring(8,10))))
						.collect(Collectors.groupingBy(MonitoredData::getActivity,Collectors.counting()));
				
				for (Entry<String, Long> mt : obj.entrySet()) 
				{
					System.out.println("Activitatea: "+mt.getKey()+" s-a desfasurat de: "+mt.getValue()+" ori.");
				}
				
				//CERINTA 4
		
				System.out.println("\n");
				System.out.println("CERINTA 4\n");
				
			Map<String, Long> map2 = 
			mo.stream()
				.collect(Collectors.groupingBy(m-> "In data de "+m.getStart_time().substring(0,10)+" activitatea "+m.getActivity()+" a fost realizata de ",Collectors.counting()));
			
			
			for (Entry<String, Long> mt : map2.entrySet()) 
			{
				System.out.println(mt+" ori");
			}
			
		
			
			//CERINTA 5
			System.out.println("\n");
			System.out.println("CERINTA 5\n");
			

			Map<String,Long> dr = 
					mo.stream() 
						.collect(Collectors.groupingBy(h-> "Din data de :" + h.getStart_time() + " pana in data de: "+h.getEnd_time() +" activitatea: "+ h.getActivity()+ " a durat: ", Collectors.summingLong(f->data(f.getEnd_time(), f.getStart_time()))));     

			
			for (Entry<String, Long> mt : dr.entrySet()) 
			{
				System.out.println(mt+ " milisecunde");
			}
			
			
		
			//CERINTA 6

			System.out.println("\n");
			System.out.println("CERINTA 6\n");
			
			Map<String, Long> duration = 
			
				mo.stream()
			.collect(Collectors.groupingBy(MonitoredData::getActivity,Collectors.summingLong(g-> data(g.getEnd_time(), g.getStart_time()))));
			
			long j =0;
			long h=0;
			 String time ="";
			for (Entry<String, Long> mt : duration.entrySet()) 
			{
				j=mt.getValue();
				j=j/1000;
				time = String.valueOf(j/3600);
				time+=" Hours ";
				time+= String.valueOf((j%3600)/60);
				time+=" MIN ";
				time+= String.valueOf((j%60));
				time+= " Seconds ";
				
				System.out.println("Activitatea "+ mt.getKey()+" s-a desfasurat, in total in:  "+time);
			}
		
			
			//CERINTA 7
			
			System.out.println("\n");
			System.out.println("CERINTA 7\n");

			
			
			 Map <String, Long> actt = mo.stream()
					.collect(Collectors.groupingBy(d->d.getActivity(),Collectors.counting()));
			
			Map <String, Long> m2 =  mo.stream()
			.filter(g-> data(g.getEnd_time(), g.getStart_time())<=300000)  // 5 MINUTE IN MILISECUNDE SUNT 300.000 
			.collect(Collectors.groupingBy(d->d.getActivity(),Collectors.counting()));
			
			ArrayList<String> dat = (ArrayList<String>) m2.entrySet().stream()
					.filter(d->d.getValue() >= 0.9*actt.get(d.getKey()))
					.map(d->d.getKey())
			        .collect(Collectors.toList());
			
			System.out.println( "Activitățiile si numarul de cate ori apar cu durata mai mica decat 5 minute: \n");
			
			for (Entry<String, Long> mt : m2.entrySet()) 
			{
				System.out.println(mt);
			}
			System.out.println("\n");
			System.out.println("Activitatiile care au in 90% din cazuri, durata mai mica decat 5 minute, sunt: \n");
			
			int nr=dat.size();
			int ii=0;
		while(ii<nr)
				{
			System.out.println(dat.get(ii).toString());
			ii++;
				}
			
	}
	

}

import java.util.*;


/*
Name: Omar Khaled Mohy Eldin
ID: 20190353
Department: CS
Section: S2
*/

public class LZW {
    private final Hashtable<String,Integer> dictionary;
    private final Hashtable<Integer,String> replica;
    private ArrayList<Integer> tags = new ArrayList<>();
    private int counter;

    public LZW(){
        dictionary = new Hashtable<>();
        replica = new Hashtable<>();
        counter=128;

        String init = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        for(int i=0; i<init.length(); i++){
            dictionary.put(Character.toString(init.charAt(i)),(int)init.charAt(i));
            replica.put((int)init.charAt(i),Character.toString(init.charAt(i)));
        }
    }

    public void printDictionary(){
        Set<Map.Entry<String,Integer>> entries = dictionary.entrySet();

        for(Map.Entry<String,Integer> entry : entries ){
            System.out.println( entry.getKey() + " : " + entry.getValue() );
        }
    }

    public void compress(String data){
        for(int i=0; i<data.length(); i++){
            int ind =0;
            String temp = Character.toString(data.charAt(i));
            if(i==0){
                tags.add(dictionary.get(Character.toString(data.charAt(i))));
                temp+= Character.toString(data.charAt(i+1));
                dictionary.put(temp,counter);
                replica.put(counter,temp);
                counter++;
                System.out.println("Added Entry to dictionary: "+temp);
            }else{
                String extra="";
                extra = temp;
                ind = i+1;
                while(ind<data.length()){
                    extra+= data.charAt(ind);
                    if(dictionary.containsKey(extra)){
                        ind++;
                    }else{
                        break;
                    }
                    i++;
                }
                if(!extra.contains("*")) {
                    tags.add(dictionary.get(extra.substring(0, extra.length() - 1)));
                    dictionary.put(extra, counter);
                    replica.put(counter,extra);
                    counter++;
                    System.out.println("Added Entry to dictionary: "+extra);
                }else{
                    tags.add(dictionary.get(extra.substring(0, extra.length() - 1)));
                }
            }
        }

        System.out.println("\n*******************************************************\n");

        Set<Map.Entry<String,Integer>> entries = dictionary.entrySet();
        for(Map.Entry<String,Integer> entry : entries){
            if(entry.getValue()>=128){
                System.out.println( entry.getKey() + " : " + entry.getValue() );
            }
        }
        System.out.println("\n*******************************************************\n");
        for(int i=0; i<tags.size(); i++){
            System.out.println(tags.get(i));
        }
    }

    public static void main(String[] args) {
        LZW lzw = new LZW();
        String data = "AABABABBCBCBBBBBBBBBBBBBBBBBCBCBCBBBCABBA*";

        lzw.compress(data);
        System.out.println("\n*********************************************************\n");

        lzw.printDictionary();
        System.out.println("\n*********************************************************\n");

        StringBuilder compare_DATA = lzw.decompress();
        System.out.println("\n");
        System.out.println((compare_DATA+"*").toString().equals( data));
    }

    public StringBuilder decompress(){
        StringBuilder original= new StringBuilder();
        for(int i=0; i<tags.size(); i++){
            if(tags.get(i)!= null){
                System.out.print(replica.get(tags.get(i)));
                original.append(replica.get(tags.get(i)));
            }
        }
        return original;
    }
}
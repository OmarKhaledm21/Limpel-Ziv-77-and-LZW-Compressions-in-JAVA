import java.util.ArrayList;


/*
Name: Omar Khaled Mohy Eldin
ID: 20190353
Department: CS
Section: S2
*/

class TagNode {
    int position;
    int length;
    char nextChar;

    public TagNode(){
        position =0;
        length =0;
        nextChar='\0';
    }

    public TagNode(int position, int length, char nextChar){
        this.position = position;
        this.length = length;
        this.nextChar = nextChar;
    }

    @Override
    public String toString() {
        return "Tag: <"+ position +","+length+","+nextChar+">";
    }
}

public class LZ77v2 {
    private ArrayList<TagNode>dictionary;

    public LZ77v2(){
        this.dictionary = new ArrayList<>();
    }

    public ArrayList<TagNode> compress(String data){
        String searchWindow="";
        String lookAheadMatch = "";
        int ind=0;
        for(int i=0; i<data.length(); i++){
            int position = 0;
            int length = 0;
            char nextChar = '\0';
            lookAheadMatch += data.charAt(i);
            if(i==0){
                searchWindow += (data.charAt(0));
                nextChar = data.charAt(0);
            }else{
                if(searchWindow.contains(lookAheadMatch)){
                    if(lookAheadMatch.length() == 1){
                        ind =i;
                    }

                    //Finish and add to dictionary if its the last loop!
                    String temp = (lookAheadMatch);
                    temp += data.charAt(i+1);
                    if(temp.endsWith("\n")) {
                       position = ind - searchWindow.lastIndexOf(lookAheadMatch.substring(0,lookAheadMatch.length()-1));
                       length = lookAheadMatch.length()-1;
                       nextChar = lookAheadMatch.charAt(lookAheadMatch.length()-1);
                       i++;
                    }else{
                        continue;
                    }
                } else {
                    int match_pos = searchWindow.indexOf(lookAheadMatch.substring(0,lookAheadMatch.length()-1));

                    if(match_pos == -1){
                        match_pos =0;
                    }

                    position = ind - match_pos;
                    nextChar = lookAheadMatch.charAt(lookAheadMatch.length()-1);

                    if(position == -1){
                        position = 0;
                    }
                    if(position != 0) {
                        length = lookAheadMatch.length()-1;
                    }

                    searchWindow += lookAheadMatch;
                    ind =0;
                }
            }

            TagNode tagNode = new TagNode(position,length,nextChar);
            dictionary.add(tagNode);
            lookAheadMatch = "";
            System.out.println(tagNode.toString());
        }

        return this.dictionary;
    }

    public static void main(String[] args) {
        LZ77v2 lz77 = new LZ77v2();

        String data = "AABABABBCBCBBBBBBBBBBBBBBBBBCBCBCBBBCABBA\n";

        ArrayList<TagNode> compressed_dictionary = lz77.compress(data);

        lz77.decompress(compressed_dictionary);



        /*
        LZ77 lz77 = new LZ77();
        System.out.println("Input some test data to be compressed and decompressed using LZ77: ");
        Scanner sc = new Scanner(System.in);
        String data = sc.next();

        ArrayList<Tag>compressed_dictionary = lz77.compress(data);

        lz77.decompress(compressed_dictionary);

        sc.close()
         */
    }

    public void decompress(ArrayList<TagNode> tags){
        StringBuilder decompressed_data = new StringBuilder("");

        for(TagNode tag : tags){
            int startTagIndex = decompressed_data.length() - tag.position;
            for(int current = 0; current < tag.length; current++){
                decompressed_data.append(decompressed_data.charAt(startTagIndex+current));
            }
            decompressed_data.append(tag.nextChar);
        }

        System.out.println("Decoded Data: "+decompressed_data.toString());
    }
}

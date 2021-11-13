import java.util.ArrayList;
import java.util.Scanner;

/*
Name: Omar Khaled Mohy Eldin
ID: 20190353
Department: CS
Section: S2
*/

class Tag {
    int position;
    int length;
    char nextChar;

    public Tag(){
        position =0;
        length =0;
        nextChar='\0';
    }

    public Tag(int position, int length, char nextChar){
        this.position = position;
        this.length = length;
        this.nextChar = nextChar;
    }

    @Override
    public String toString() {
        return "Tag: <"+ position +","+length+","+nextChar+">";
    }
}

public class LZ77 {
    private ArrayList<Tag> dictionary;

    public LZ77(){
        dictionary = new ArrayList<>();
    }

    public ArrayList<Tag> compress(String data) {
        int cursor = 0;
        while(cursor < data.length()) {
            int position = Match_Seq(cursor, data)[0];
            int length = Match_Seq(cursor, data)[1];
            char nextChar = '\0';
            if (!((cursor + length) >= data.length())) {
                nextChar = data.charAt(cursor + length);
            }
            Tag entry = new Tag(position, length, nextChar);
            dictionary.add(entry);
            System.out.println(entry.toString());
            cursor += (++length);
        }
        return dictionary;
    }

    private int[] Match_Seq(int ind, String data) {
        int finalDistance = 0,finalLength = 0, currDistance = 0, currLength = 0;
        int position = ind-1;
        while(position >= 0) {
            if(data.charAt(ind) == data.charAt(position)){
                int searchWindowIndex = position+1;
                int lookAheadIndex = ind+1;
                currDistance = ind - position;
                currLength = 1;
                if(lookAheadIndex < data.length()) {
                    while(data.charAt(searchWindowIndex) == data.charAt(lookAheadIndex)){
                        currLength++;
                        lookAheadIndex++;
                        searchWindowIndex++;
                    }
                }
                if(currLength > finalLength){
                    finalLength = currLength;
                    finalDistance = currDistance;
                }
            }
            position--;
        }
        int[] temp = {finalDistance, finalLength};

        return temp;
    }



    public static void main(String[] args) {
        LZ77 lz77 = new LZ77();

        String data = "ABAABABAABBBBBBBBBBBBA";

        ArrayList<Tag> compressed_dictionary = lz77.compress(data);

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

    public void decompress(ArrayList<Tag> tags){
        StringBuilder decompressed_data = new StringBuilder("");

        for(Tag tag : tags){
            int startTagIndex = decompressed_data.length() - tag.position;
            for(int current = 0; current < tag.length; current++){
                decompressed_data.append(decompressed_data.charAt(startTagIndex+current));
            }
            decompressed_data.append(tag.nextChar);
        }

        System.out.println("Decoded Data: "+decompressed_data.toString());
    }
}

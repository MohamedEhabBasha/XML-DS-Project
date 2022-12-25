package com.mycompany.huffman;

import java.util.Arrays;
import java.util.Comparator;  
import java.util.HashMap;  
import java.util.LinkedHashSet;
import java.util.Map;  
import java.util.PriorityQueue;  
import java.util.Set;
import java.util.stream.Collectors;
//defining a class that creates nodes of the tree  
class Node  
{  
    //storing character in ch variable of type character  
    Character ch;  
    //storing frequency in freq variable of type int  
    Integer freq;  
    //initially both child (left and right) are null  
    Node left = null;   
    Node right = null;   
    //creating a constructor of the Node class  
    Node(Character ch, Integer freq)  
    {  
        this.ch = ch;  
        this.freq = freq;  
    }  
    //creating a constructor of the Node class  
    public Node(Character ch, Integer freq, Node left, Node right)  
    {  
        this.ch = ch;  
        this.freq = freq;  
        this.left = left;  
        this.right = right;  
    }  
}  
//main class  
public class HuffmanCode  
{  
    
    //function to build Huffman tree  
    public static void createHuffmanTree(String text)  
    {  
        //base case: if user does not provides string  
        if (text == null || text.length() == 0)   
        {  
            return;  
        }  
        //count the frequency of appearance of each character and store it in a map  
        //creating an instance of the Map   
        Map<Character, Integer> freq = new HashMap<>();  
        //loop iterates over the string and converts the text into character array  
        for (char c: text.toCharArray())   
        {  
            //storing character and their frequency into Map by invoking the put() method   
            freq.put(c, freq.getOrDefault(c, 0) + 1);  
        }  
        //create a priority queue that stores current nodes of the Huffman tree.  
        //here a point to note that the highest priority means the lowest frequency   
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(l -> l.freq));  
        //loop iterate over the Map and returns a Set view of the mappings contained in this Map  
        for (var entry: freq.entrySet())   
        {  
            //creates a leaf node and add it to the queue  
            pq.add(new Node(entry.getKey(), entry.getValue()));  
        }  
        //while loop runs until there is more than one node in the queue  
        while (pq.size() != 1)  
        {  
            //removing the nodes having the highest priority (the lowest frequency) from the queue  
            Node left = pq.poll();  
            Node right = pq.poll();  
            //create a new internal node with these two nodes as children and with a frequency equal to the sum of both nodes' frequencies. Add the new node to the priority queue.  
            //sum up the frequency of the nodes (left and right) that we have deleted   
            int sum = left.freq + right.freq;  
            //adding a new internal node (deleted nodes i.e. right and left) to the queue with a frequency that is equal to the sum of both nodes  
            pq.add(new Node(null, sum, left, right));  
        }  
        //root stores pointer to the root of Huffman Tree  
        Node root = pq.peek();  
        //trace over the Huffman tree and store the Huffman codes in a map  
        Map<Character, String> huffmanCode = new HashMap<>();  
        encodeData(root, "", huffmanCode);  
        //print the Huffman codes for the characters  
        System.out.println("Huffman Codes of the characters are:\n" + huffmanCode);
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        //prints the initial data  
        System.out.println("The initial string is:\n" + text);
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("The Size of xml file is: " + (text.length()*8)+" bits = "+text.length()+" Bytes");
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        //creating an instance of the StringBuilder class   
        StringBuilder sb = new StringBuilder();  
        //loop iterate over the character array  
        for (char c: text.toCharArray())   
        {  
            //prints encoded string by getting characters   
            sb.append(huffmanCode.get(c));  
        }   
        System.out.println("The encoded string is:\n" + sb);
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("the compressd String is:\n"+binary2String(sb));
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("the number of character without repetition: " + countNotRepeatedCh(text)+" Character = " + (countNotRepeatedCh(text)*8)+" bits");
        System.out.println("The number of encoded bits is: " + sb.length()+" bits");
        System.out.println("The size of encoded(compressed)file is: " + (sb.length()+(countNotRepeatedCh(text)*8))+" bits = "+(sb.length()/8.0+(countNotRepeatedCh(text)))+" Bytes");  
        System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.print("The decoded string is:\n");  
        if (isLeaf(root))  
        {  
            //special case: For input like a, aa, aaa, etc.  
            while (root.freq-- > 0)   
            {  
                System.out.print(root.ch);  
            }  
        }  
        else   
        {  
            //traverse over the Huffman tree again and this time, decode the encoded string  
            int index = -1;  
            while (index < sb.length() - 1)   
            {  
                index = decodeData(root, index, sb);  
            }  
        }  
    } 
    
    
    
    
//traverse the Huffman Tree and store Huffman Codes in a Map  
    //function that encodes the data  
    public static void encodeData(Node root, String str, Map<Character, String> huffmanCode)  
    {  
        if (root == null)   
        {  
            return;  
        }  
        //checks if the node is a leaf node or not  
        if (isLeaf(root))   
        {  
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");  
        }  
        encodeData(root.left, str + '0', huffmanCode);  
        encodeData(root.right, str + '1', huffmanCode);  
    }  
    
    
    
    
    //traverse the encoded String to String
    public static String binary2String(StringBuilder sb){
        String str = "";
        for (int i = 0; i < sb.length()/8; i++) {
        int a = Integer.parseInt(sb.substring(8*i,(i+1)*8),2);
        str += (char)(a);
    }
        return str;
    }
    
    
    
    public static int countNotRepeatedCh(String str){
                Set<String> set = Arrays.stream(str.split("")).collect(Collectors.toCollection(LinkedHashSet::new));
                return set.size();
                
            }
    
    
    
    
    //traverse the Huffman Tree and decode the encoded string function that decodes the encoded data  
    public static int decodeData(Node root, int index, StringBuilder sb)  
    {  
        //checks if the root node is null or not  
        if (root == null)   
        {  
            return index;  
        }   
        //checks if the node is a leaf node or not  
        if (isLeaf(root))  
        {  
            System.out.print(root.ch);  
            return index;  
        }  
        index++;   
        root = (sb.charAt(index) == '0') ? root.left : root.right;  
        index = decodeData(root, index, sb);  
        return index;  
    }  
    
    
    
    
    //function to check if the Huffman Tree contains a single node  
    public static boolean isLeaf(Node root)   
    {  
        //returns true if both conditions return ture  
        return root.left == null && root.right == null;  
    } 
    
    
    
    
    // function that remove new lines and spaces
    static String RemoveSpace(String data){
        
        String str1 = data.replaceAll("\n", "");
        String str2 =str1.replaceAll(" ", "");
        return str2;
    }
    
    //driver code  
    public static void main(String args[])  
    {  
        String text = "<users>\n" +
"    <user>\n" +
"        <id>1</id>\n" +
"        <name>Ahmed Ali</name>\n" +
"        <posts>\n" +
"            <post>\n" +
"                <body>\n" +
"                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
"                </body>\n" +
"                <topics>\n" +
"                    <topic>\n" +
"                        economy\n" +
"                    </topic>\n" +
"                    <topic>\n" +
"                        finance\n" +
"                    </topic>\n" +
"                </topics>\n" +
"            </post>\n" +
"            <post>\n" +
"                <body>\n" +
"                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
"                </body>\n" +
"                <topics>\n" +
"                    <topic>\n" +
"                        solar_energy\n" +
"                    </topic>\n" +
"                </topics>\n" +
"            </post>\n" +
"        </posts>\n" +
"        <followers>\n" +
"            <follower>\n" +
"                <id>2</id>\n" +
"            </follower>\n" +
"            <follower>\n" +
"                <id>3</id>\n" +
"            </follower>\n" +
"        </followers>\n" +
"    </user>\n" +
"    <user>\n" +
"        <id>2</id>\n" +
"        <name>Yasser Ahmed</name>\n" +
"        <posts>\n" +
"            <post>\n" +
"                <body>\n" +
"                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
"                </body>\n" +
"                <topics>\n" +
"                    <topic>\n" +
"                        education\n" +
"                    </topic>\n" +
"                </topics>\n" +
"            </post>\n" +
"        </posts>\n" +
"        <followers>\n" +
"            <follower>\n" +
"                <id>1</id>\n" +
"            </follower>\n" +
"        </followers>\n" +
"    </user>\n" +
"    <user>\n" +
"        <id>3</id>\n" +
"        <name>Mohamed Sherif</name>\n" +
"        <posts>\n" +
"            <post>\n" +
"                <body>\n" +
"                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.\n" +
"                </body>\n" +
"                <topics>\n" +
"                    <topic>\n" +
"                        sports\n" +
"                    </topic>\n" +
"                </topics>\n" +
"            </post>\n" +
"        </posts>\n" +
"        <followers>\n" +
"            <follower>\n" +
"                <id>1</id>\n" +
"            </follower>\n" +
"        </followers>\n" +
"    </user>\n" +
"</users>";  
        createHuffmanTree(text);
        System.out.println("\n---------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Minifying the XML file:\n"+RemoveSpace(text));
    }  
}  
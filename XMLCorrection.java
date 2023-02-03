import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.util.*;
public class XMLCorrection {

    static Vector<String> isolateLines(Vector<String> file_lines){
	//this method returns new array list where each element represent a line that is 
	//<>data<> but needs correction
	//<>data but may need closing
	//data only it's then correct
	//data<> but may need correction
	// single open or closing then it may need correction using the stack
	
	Vector<String> isolated_lines = new Vector<>(); 
	for(int i = 0; i< file_lines.size(); i++){
	
	//result[0] open tag if exist otherwise ""
	//result[1] data if exist otherwise ""
	//result[2] closing tag if exist otherwise ""
		String me = "";
		Vector<String> result = identifyLine(file_lines.get(i));
		if(result.get(0)!="")me+=result.get(0);
		if(result.get(1)!="")me+=result.get(1);
		if(result.get(2)!="")me+=result.get(2);
		isolated_lines.add(me);
		if(i+1 >= file_lines.size() && result.get(3) != "")file_lines.add("");
		if(result.get(3) != "")file_lines.set(i+1,result.get(3)+file_lines.get(i+1));
	}
	return isolated_lines;
}
static Vector<String> identifyLine(String corrupted_line){
	//this method takes a single string that may contains many tags 
	//and tries to make a single string with at most <tag>data</tag>
	//and add the remaining to the next line to be processed on the next iteration of for loop
	//it iterate over the line until find < and then > and read string between <> and return it 
	//as open tag in res.get(0)
	//the same as </tag>
	// and any this between > of open tag and < of closed tag as data in res.get(1)
	//and the remaining is added to res.get(3)
	//then return the res
	Boolean dataStart = false;
	Boolean openTagStart = false;
	Boolean closeTagStart = false;
	String data = "";
	String openTag = "";
	String closeTag = "";
	String to_be_added_to_next_string = "";
	for(int i = 0; i< corrupted_line.length(); i++){
	    if(!openTagStart && !closeTagStart && !dataStart && corrupted_line.charAt(i) == ' ') continue;
		if(!dataStart  && (!openTagStart && !closeTagStart)&&(
			('a'<=corrupted_line.charAt(i)&&corrupted_line.charAt(i)<='z') || 
			('A'<=corrupted_line.charAt(i)&&corrupted_line.charAt(i)<='Z') ||
			('0'<=corrupted_line.charAt(i)&&corrupted_line.charAt(i)<='9')
			)
		){
			dataStart = true;
		}
		else if(corrupted_line.charAt(i) == '<'){
			if(dataStart){
				dataStart = false;
				closeTagStart = true;
			}
			else{
			    if(openTag != ""){
			        to_be_added_to_next_string = corrupted_line.substring(i, corrupted_line.length());

			        break;
			    }
				openTagStart = true;
			}
		}
		if(dataStart){
			data+=corrupted_line.charAt(i);
		}
		else if(openTagStart){
			if(corrupted_line.charAt(i) == '>'){
				openTagStart = false;
				
				if(openTag.contains("/")) {
					openTag+= ">";
					closeTag = openTag;
					openTag = "";
					break;
				}
			}
			openTag += corrupted_line.charAt(i);
			
		}
		else if(closeTagStart){
			if(corrupted_line.charAt(i) == '>'){
				closeTag += '>';
				to_be_added_to_next_string = corrupted_line.substring(i+1, corrupted_line.length() );
				
				break;
			}
			closeTag += corrupted_line.charAt(i);
		}
		
	}
	
	Vector<String> res = new Vector<>();
	res.add(openTag);
	res.add(data);
	res.add(closeTag);
	res.add(to_be_added_to_next_string.trim());
	
	return res;
}
public static Vector<String> combineLines(Vector<String> iso_lines){
	//this method combines line with next line if it is possible 
	//it takes vector of strings and keep adding  each line with its next and remove next 
	// if it is valid
	//then return new list of processed lines
	for(int i = 1; i < iso_lines.size(); i++) {
		String s1 = iso_lines.get(i-1);
		String s2 = iso_lines.get(i);
		Vector<String> s1_elem = identifyLine(s1);
		Vector<String> s2_elem = identifyLine(s2);
		
		if(s1_elem.get(2) == "" && s2_elem.get(0) == "") {
			iso_lines.set(i-1, s1 + s2);
			iso_lines.remove(i);
			i--;
		}
	}
	return iso_lines;
}

public static Vector<String> stack_step(Vector<String> corrected_lines){
	//this methods complete missing closed tags and removes redundant closed tags
	//closed tags with no data and open tag
	//by using stack it keeps pushing open tags in the stack and pop 
	//in case of closed tag it keeps poping until found the matching open tag 
	//and adding the closed tag for every open tag in the stack 
	// if open tag not found and stack becomes empty it removes this closed tag from
	//the file (redundant closed tag)
	Stack<String> stk = new Stack<String>();
	for(int i = 0; i< corrected_lines.size(); i++) {
		Vector<String> elem = identifyLine(corrected_lines.get(i));
		
		
		if(elem.get(0) != "" && elem.get(2) != "") continue;
		else if(elem.get(0) != "") {
			stk.push(elem.get(0));
		}
		else {
			
			String res = "";
	
			while(!stk.empty()&&stk.peek().substring(1, stk.peek().length() -1) != elem.get(2).substring(2, elem.get(2).length() - 1)) {
				
				res += ("</" + stk.peek().substring(1, stk.peek().length() - 1) + ">");
				stk.pop();
			}
			if(!stk.empty())stk.pop();
			corrected_lines.set(i, res);
			
		}
	}
	String res = "";
	while(!stk.empty()) {
		
		res += ("</" + stk.peek().substring(1, stk.peek().length() - 1) + ">");
		corrected_lines.add(res);
		stk.pop();
	}
	
	return corrected_lines;
	
}
public static Vector<String> special_case(Vector<String> corrected_lines){
    //search for case of data on multiple line with no open tag before and then add open tag
    //if line has no end tag and the next line has no open tag then combine lines
    //<>data
    //data
    //data
    // res = <> data + data + data
    for(int i = 0; i < corrected_lines.size()-1; i++){
        Vector<String> res = identifyLine(corrected_lines.get(i));
        Vector<String> next = identifyLine(corrected_lines.get(i+1));
        if(res.get(2) == "" && next.get(0) == ""){
            corrected_lines.set(i, corrected_lines.get(i)+corrected_lines.get(i+1));
            corrected_lines.remove(i+1);
            i--;
        }
    }
    return corrected_lines;
}
public static Vector<String> repair(Vector<String> res){
	//this method adds closed tag if it's required if the line has open or closing but the other tag is missing 
    //<tag>data
    // res <tag>data</tag>
    Vector<String> me = new Vector<>();
    me.add(res.get(0));
    me.add(res.get(1));
    me.add(res.get(2));
    if(res.get(1) != "" && (res.get(0)!= "" || res.get(2) !="")){
        String tag;
            String str = "";
            String tempStr = me.get(0) == ""?me.get(2):me.get(0);
            
            for(int i = 0; i< me.get(0).length(); i++){
                if(tempStr.charAt(i) == '>') break;
                else if(tempStr.charAt(i) == '/' || tempStr.charAt(i) == '<') continue;
                str+= tempStr.charAt(i);
            }
            me.set(0, "<" + str + ">");
            me.set(2, "</" +str + ">" );
            return me;
        
    }
    else return me;
    
}
public static Vector<String> correctXML(Vector<String> file_lines){
	//this method is used to call the methods used in correction and 
	// return vector of corrected string 
  Vector<String> iso_lines = isolateLines(file_lines);
  iso_lines = special_case(iso_lines);
  iso_lines = combineLines(iso_lines);
  for(int i = 0; i < iso_lines.size(); i++){
    Vector<String> completeLine = identifyLine(iso_lines.get(i));
    completeLine = repair(completeLine);
    iso_lines.set(i, completeLine.get(0) + completeLine.get(1) + completeLine.get(2));
  }
  iso_lines = stack_step(iso_lines);
  return iso_lines;
}
public static Vector<String> stringToVecString(String given) {
	//convert a string into vector of string 
	String name = "conan.xml";
	File vodka = new File(name);
	 try {
	      boolean value = vodka.createNewFile();
	      if (value) {
	        System.out.println("New Java File is created.");
	      }
	      else {
	        System.out.println("The file already exists.");
	      }
	    }
	    catch(Exception e) {
	      e.getStackTrace();
	    }
	try {
		FileWriter output = new FileWriter(name);
		
		output.write(given);
		output.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
    FileInputStream fis = null;
    BufferedReader reader = null;
    Vector<String> file = new Vector<>();
    try {
        fis = new FileInputStream(name);
        reader = new BufferedReader(new InputStreamReader(fis));
        String line = reader.readLine();
        while(line != null){
        	file.add(line);
           // System.out.println(line);
            line = reader.readLine();
        }          
     
    } catch (FileNotFoundException ex) {
    	 System.out.println("Not Found");
    } catch (IOException ex) {
        
     
    } finally {
        try {
            reader.close();
            fis.close();
        } catch (IOException ex) {
            
        }
    }
    return file;
}
public static String correctXMLAPI(String given)
{
	//used by GUI to correct the given file(as string) and return the corrected file(as string)
	Vector<String> fl = correctXML(stringToVecString(given));
	String str = "";
	for(int i = 0;i < fl.size(); i++) {
		str+= fl.get(i);
	}
	return str;
}
public static String correctXMLAPIPath(String path)
{
	//same as correctXMLAPI but it takes path
	FileInputStream fis = null;
    BufferedReader reader = null;
    Vector<String> file = new Vector<>();
    try {
        fis = new FileInputStream(path);
        reader = new BufferedReader(new InputStreamReader(fis));
        String line = reader.readLine();
        while(line != null){
        	file.add(line);
           // System.out.println(line);
            line = reader.readLine();
        }          
     
    } catch (FileNotFoundException ex) {
    	 System.out.println("Not Found");
    } catch (IOException ex) {
        
     
    } finally {
        try {
            reader.close();
            fis.close();
        } catch (IOException ex) {
            
        }
    }
    
    Vector<String> corrected = correctXML(file);
    String ret = "";
    for(int i = 0; i< corrected.size(); i++) {
    	ret += corrected.get(i);
    }
    return ret;
}
}


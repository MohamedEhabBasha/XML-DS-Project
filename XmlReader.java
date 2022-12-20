package finalproject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.*;
public class XmlReader {
	static String errorByString(String gin) {
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
			output.write(gin);
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
        file = checkError(file);
		
		return toString(file);
	}
	static String errorByPath(String path) {
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
        file = checkError(file);
        return toString(file);
	}
	
	static String toString(Vector<String> v) {
		String answer = "";
		for(int i=0;i<v.size();i++) {
			answer+=v.get(i);
			answer+="\n";
		}
		return answer;
	}
	static Vector<String> checkError(Vector<String> v){
		Vector<String> file = v;
		//Stack<String> s = new Stack<>();
		Vector<Pair> data = new Vector<>();
		int n = file.size();
		for(int i=0;i<n;i++) {
			String temp = file.get(i);
			if(openTag(temp)) {
				data.add(new Pair(getTag(temp),i));
			}
			else if(closeTag(temp)) {
				String g =getTag(temp);
				if(search(data,g)) {
					int nn = data.size();
					for(int ii=nn-1;ii>=0;ii--) {
						if(!equalTags(data.get(ii).getKey(),g)) {
							file.set(data.get(ii).getValue(), addError(file.get(data.get(ii).getValue())));
							data.remove(ii);
						}else {
							data.remove(ii);
							break;
						}
					}
				}else {
					file.set(i, addError(temp));
				}
			}else if(hasOnlyData(temp)) {}
			else if(onlySpace(temp)) {

			}
			else {
				Vector<String> arr = identifyLine(temp);
				for(String e : arr) {
					if(openTag(e)) data.add(new Pair(e,i));
					else {
						if(search(data,e)) {
							int nn = data.size();
							for(int ii=nn-1;ii>=0;ii--) {
								if(!equalTags(data.get(ii).getKey(),e)) {
									file.set(data.get(ii).getValue(), addError(file.get(data.get(ii).getValue())));
									data.remove(ii);
								}else {
									data.remove(ii);
									break;
								}
							}
						}else {
							if(hasError(temp))	file.set(i, addError(temp));
						}
					}
				}
			}
		}
		return file;
	}
	static boolean hasError(String line) {
		for(int i=0;i<line.length();i++) {
			if(line.charAt(i) == '?') return true;
		}
		return false;
	}
	static boolean search(Vector<Pair> v,String s) {
		int size = v.size();
		for(int i=size-1;i>=0;i--) {
			if(equalTags(v.get(i).getKey(),s)) {
				return true;
			}
		}
		return false;
	}
	static String addError(String line) {
		return line + "   ?Error";
	}
	static boolean oneTag(String line) {
		int size = line.length();
		int count = 0;
		for(int i=0;i<size;i++) {
			if(line.charAt(i) == '<') count++;
		}
		return count == 1;
	}
	static boolean equalTags(String open,String close) {
		String o = open;
		String c = "";
		if(open.equals(close)) return false;
		for(int i=0;i<close.length();i++) {
			if(close.charAt(i) != '/')
				c+=close.charAt(i);
		}
		return o.equals(c);
	}

	static boolean hasData(String line) {
		int size = line.length();
		int l=0;
		while(l<size) {
			while(l<size && line.charAt(l) != '>') l++;
			l++;
			while(l<size && line.charAt(l) != '<') {
				if(line.charAt(l) != ' ' )
					return true;
				l++;
			}
			l++;
		}
		return false;
	}
	static boolean hasOnlyData(String line) {
		int size = line.length();
		boolean ok = false;
		for(int i=0;i<size;i++) {
			if(line.charAt(i) == '<') return false;
			if(line.charAt(i) != ' ') ok = true;
		}
		return ok;
	}
	static boolean openTag(String line) {
		int size = line.length();
		int l=0;
		boolean ok = false;
		while(l<size && line.charAt(l) != '<') {
			if(line.charAt(l) != ' ') return false;
			l++;
		}l++;
		if(l<size && line.charAt(l) != '/') {
			ok = true;
		}
		while(l<size && line.charAt(l) != '>') l++;
		l++;
		while(l<size) {
			if(line.charAt(l) != ' ') return false;
			l++;
		}
		return ok;
	}
	static boolean closeTag(String line) {
		int size = line.length();
		int l=0;
		boolean ok = false;
		while(l<size && line.charAt(l) != '<') {
			if(line.charAt(l) != ' ') return false;
			l++;
		}l++;
		if(l<size && line.charAt(l) == '/') {
			ok = true;
		}
		while(l<size && line.charAt(l) != '>') l++;
		l++;
		while(l<size) {
			if(line.charAt(l) != ' ') return false;
			l++;
		}
		return ok;
	}
	static String getTag(String line) {
		String tag = "";
		int size = line.length();
		int l=0;
		while(l<size && line.charAt(l) != '<') l++;
		while(l<size && line.charAt(l) != '>') {
			tag += line.charAt(l);
			l++;
		}tag += line.charAt(l);
		return tag;
	}
	static boolean onlySpace(String line) {
		int size = line.length();
		for(int i=0;i<size;i++) {
			if(line.charAt(i) != ' ')
				return false;
		}
		return true;
	}
	static Vector<String> identifyLine(String line){
		Vector<String> v = new Vector<>();
		int n = line.length(),l = 0;
		while(l<n) {
			while((l<n)&&line.charAt(l) != '<')l++;
			String tag = "";
			while((l<n)&&line.charAt(l) != '>') {
				tag+= line.charAt(l);
				l++;
			}
			if(l<n) {
				tag+= line.charAt(l);
				v.add(tag);
			}
			l++;
		}
			
		return v;
	}
}
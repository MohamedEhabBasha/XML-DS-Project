package finalproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;
import java.util.Stack;

public class ToJSON {
	static String stringJson(String data) {
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
			output.write(data);
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
        file = toJson(file);
        file = editComma(file);
        file = tabing(file);
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
	static Vector<String> toJson(Vector<String> v){
		Vector<String> jsonFile = new Vector<>();
		Vector<String> file = v;
		Stack<String> s  = new Stack<>();
		Vector<Pair> data = new Vector<>();
		jsonFile.add("{");
		int n = file.size();
		for(int i=0;i<n;i++) {
			String temp = file.get(i);
			
			if(openTag(temp)) {
				if(data.size()==0) {
					jsonFile.add(jsonOpenTag(temp));
					data.add(new Pair(getTag(temp),i));
				}
				else {
					String peek = getTag(temp);
					Vector<Pair> vv = basha(file,data.get(data.size()-1).getValue());
					int y = vv.get(posPair(vv,peek)).getValue();
					if(y > 1) {
						jsonFile.add("{");
						data.add(new Pair(getTag(temp)+'?',i));
					}else data.add(new Pair(getTag(temp),i));
					jsonFile.add(jsonOpenTag(temp));
				}
				//s.push(getTag(temp));
			}else if(closeTag(temp)) {
				String line = "";
				String ha = data.get(data.size()-1).getKey();
				
				if(isList(temp)) {
					line += "]";
					jsonFile.add(line);
				}else {
					line += "}";
					jsonFile.add(line);
					if(ha.charAt(ha.length()-1) == '?')
						jsonFile.add(line+',');
				}
				
				data.remove(data.size()-1);
			}else if(hasOnlyData(temp)) {
				String jojo = "\"";
				for(int ii=0;ii<temp.length();ii++) {
					if(temp.charAt(ii) != ' ')jojo+=temp.charAt(ii);
				}
				jojo+="\"";
				jsonFile.add(jojo);
			}else {
				Vector<String> kg = identifyLine(temp);
				for(String e : kg) {
					if(openTag(e)) {
						String peek = getTag(e);
						Vector<Pair> vv = basha(file,data.get(data.size()-1).getValue());
						int y = vv.get(posPair(vv,peek)).getValue();
						if(y>1) {
							jsonFile.add("{");
							jsonFile.add(jsonOpenTagg(peek) +getData(temp));
							jsonFile.add("},");
						}else jsonFile.add(jsonOpenTagg(peek)+getData(temp)+',');
					}
				}
			}
		}
		jsonFile.add("}");
		return jsonFile;
	}
	/*static boolean hasData(Vector<String> v,int pos) {
		String ss = getTag(v.get(pos));
		
	}*/
	static Vector<String> tabing(Vector<String> v){
		Vector<String> ans = new Vector<>();
		Stack<Integer> in = new Stack<>();
		Stack<Integer> out = new Stack<>();
		
		out.push(3);
		ans.add(v.get(0));
		for(int i=1;i<v.size()-1;i++) {
			if(c88(v.get(i)) && cb(v.get(i)) || v.get(i).equals("[") || v.get(i).equals("{")) {
				int curr = out.peek();
				in.push(curr);
				ans.add(edit(curr,v.get(i)));
				curr+=v.get(i).length()/2+3;
				out.push(curr);
			}else if(c88(v.get(i))) {
				int curr = out.peek();
				ans.add(edit(curr,v.get(i)));;
			}
			else if(v.get(i).equals("]") || v.get(i).equals("}") || v.get(i).equals("},") || v.get(i).equals("],")){
				int curr = in.peek();
				ans.add(edit(curr,v.get(i)));
				in.pop();
				out.pop();
			}else {
				ans.add(edit(in.peek(),v.get(i)));
			}
		}
		ans.add("}");
		return ans;
	}
	//---
	static Vector<String> editComma(Vector<String> v){
		Vector<String> file = v;
		for(int i = 0;i<file.size();i++) {
			String lolo = file.get(i);
			if(lolo.equals("}")) {
				if(i+1<file.size() && (file.get(i+1).equals("{") || c88(file.get(i+1))))
					file.set(i, lolo + ',');
			}else if(lolo.equals("},")) {
				if(i+1<file.size() &&( file.get(i+1).equals("}") || file.get(i+1).equals("]")))
					file.set(i, "}");
			}else if(lolo.equals("]")) {
				if(i+1<file.size() && (file.get(i+1).equals("{") || c88(file.get(i+1))))
					file.set(i, lolo + ',');
			}else if(c88(lolo)&&!cb(lolo)) {
				if(i+1<file.size() && (file.get(i+1).equals("}")||file.get(i+1).equals("]"))) {
					String happy = "";
					for(int ii=0;ii<lolo.length()-1;ii++)happy += lolo.charAt(ii);
					file.set(i, happy);
				}
					
			}
		}
		
		return file;
	}
	static boolean cb(String s) {
		for(int i=0;i<s.length();i++)
			if(s.charAt(i)=='{' || s.charAt(i)=='[') return  true;
		return false;
	}
	static boolean c88(String s) {
		for(int i=0;i<s.length();i++)
			if(s.charAt(i)==':') return  true;
		return false;
	}
	static String edit(int count,String ss) {
		String koko = "";
		int j = 0;
		while(j<count) {
			koko+=" ";
			j++;
		}
		koko+=ss;
		return koko;
	}
	//-----
	static String getData(String line) {
		int l = 0;
		String ans = "\"";
		while(l<line.length()) {
			while(line.charAt(l) != '>')l++;
			l++;
			while(line.charAt(l) != '<') {
				ans+=line.charAt(l);
				l++;
			}
			ans+="\"";
			break;
		}
		return ans;
	}
	static String jsonOpenTag(String temp) {
		String nasser = "\"";
		String ll = getTag(temp);
		for(int ii=1;ii<ll.length()-1;ii++) {
			nasser+= ll.charAt(ii);
		}
		nasser+="\"";
		String line = nasser + ": ";	
		if(isList(temp)) {
			line += "[";
		}else {
			line += "{";
		}
		return line;
	}
	static String jsonOpenTagg(String temp) {
		String nasser = "\"";
		String ll = getTag(temp);
		for(int ii=1;ii<ll.length()-1;ii++) {
			nasser+= ll.charAt(ii);
		}
		nasser+="\"";
		String line = nasser + ": ";	
		return line;
	}
	static boolean isList(String name) {
		String k = getTag(name);
		return k.charAt(k.length()-2) == 's';
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
	static Vector<Pair> basha(Vector<String> v,int pos){
		String uuu = v.get(pos);
		Vector<Pair> tags = new Vector<>();
		for(int i=pos+1;i<v.size();i++) {
			String temp = v.get(i);
			if(openTag(temp)) {
				String k = getTag(temp);
				if(!checkKey(tags,k))	tags.add(new Pair(k,1));
				else tags.set(posPair(tags,k), new Pair(k,tags.get(posPair(tags,k)).getValue()+1));
				for(int j = i+1;j<v.size();j++) {
					String kk = v.get(j);
					if(closeTag(kk)) {
						if(equalTags(k,getTag(kk))) {
							i = j;
							break;
						}
					}

				}
			}else if(closeTag(temp)) {
				if(equalTags(getTag(uuu),getTag(temp))) {
					break;
				}
			}else if (!hasOnlyData(temp)){
				Vector<String> kg = identifyLine(temp);
				for(String e : kg) {
					if(openTag(e)) {
						String k = getTag(temp);
						if(!checkKey(tags,k))	tags.add(new Pair(k,1));
						else tags.set(posPair(tags,k), new Pair(k,tags.get(posPair(tags,k)).getValue()+1));
					}
				}
			}
		}
		
		return tags;
		
	}
	static int posPair(Vector<Pair> v,String key) {
		for(int i=0;i<v.size();i++) {
			if(v.get(i).getKey().equals(key))
				return i;
		}
		return -1;
	}
	static boolean checkKey(Vector<Pair> v,String key) {
		for(int i=0;i<v.size();i++) {
			if(v.get(i).getKey().equals(key))
				return true;
		}
		return false;
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
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        FileInputStream fis = null;
        BufferedReader reader = null;
        Vector<String> file = new Vector<>();
        //C:\\Users\\DELL\\Desktop\\sample.xml
        try {
        	
            fis = new FileInputStream("C:\\Users\\DELL\\Desktop\\Notes\\sample.xml");
            
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
        
        Vector<String> nnn = new Vector<>();
        nnn.add("<names>");
        nnn.add("<name>");
        nnn.add("<id>");
        nnn.add("123");
        nnn.add("</id>");
        nnn.add("<id>");
        nnn.add("123");
        nnn.add("</id>");
        nnn.add("<id>1234</id>");
        nnn.add("</name>");
        
        nnn.add("</names>");

        Vector<Pair> nasser = new Vector<>();

		System.out.println(stringJson(toString(file)));
	}

}

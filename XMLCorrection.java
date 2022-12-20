
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
			}
			openTag += corrupted_line.charAt(i);
			//System.out.println("look " +corrupted_line.charAt(i));
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
	//System.out.println(closeTag);
	return res;
}
public static Vector<String> stack_step(Vector<String> corrected_lines){
    Stack<String> stk = new Stack<>();
    Stack<Integer> indexs = new Stack<>();
    for(int i = 0; i <  corrected_lines.size(); i++){
        Vector<String> res = identifyLine(corrected_lines.get(i));
        if(res.get(0) != "" && res.get(2) != "") continue;
        if(res.get(0) != ""){
            stk.push(res.get(0));
            indexs.push(i);
        }
        else if(res.get(2) != "" && res.get(2) != stk.peek()){
            corrected_lines.remove(i);
        }
        else if(res.get(2) == stk.peek()) stk.pop();
    }
    while(!stk.empty()){
        String str = "</" + stk.peek().substring(1, stk.peek().length());
        corrected_lines.add(str);
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
  Vector<String> iso_lines = isolateLines(file_lines);
  iso_lines = special_case(iso_lines);
  for(int i = 0; i < iso_lines.size(); i++){
    Vector<String> completeLine = identifyLine(iso_lines.get(i));
    completeLine = repair(completeLine);
    iso_lines.set(i, completeLine.get(0) + completeLine.get(1) + completeLine.get(2));
  }
  iso_lines = stack_step(iso_lines);
  return iso_lines;
}
    

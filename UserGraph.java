package finalproject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class UserGraph {
	private static HashMap<Integer,User> userById = new HashMap<>();
	public static Vector<User> createGraph(Vector<String> data){
		data = format(data);
		Vector<User> users = new Vector<>();
		int size = data.size();
		for(int i=0;i<size;i++) {
			String line = data.get(i);
			if(line.contains("<user>")) {
				User user = new User();
				for(int j = i;j<size;j++) {
					String inner = data.get(j);
					if(inner.contains("</user>")) {
						break;
					}
					if(inner.contains("<id>")) {
						user.setId(Integer.parseInt(getData(data,j)));
					}else if(inner.contains("<body>")) {
						user.addPosts(getData(data,j));
					}else if(inner.contains("<topic>")) {
						user.addTopics(getData(data,j));
					}else if(inner.contains("<name>")) {
						user.setName(getData(data,j));
					}else if(inner.contains("<follower>")) {
						for(int k = j;k<size;k++,j++) {
							String in = data.get(k);
							if(in.contains("<id>")) {
								int o = Integer.parseInt(getData(data,j));
								user.addFollowers(o);
							}
							else if(in.contains("</follower>")) break;
						}
					}
				}
				users.add(user);
			}
		}
		
		return users;
	}
	private static String getData(Vector<String> data,int index) {
		String read = "";
		int size = data.size();
		boolean start = false;
		for(int i = index;i<size;i++) {
			String line = data.get(i);

			for(int j = 0;j<line.length();j++) {
				if(line.charAt(j) == '>') start = true;
				else if(start){
					if(line.charAt(j) != '<') {
						read+=line.charAt(j);
					}else return read.trim();
				}
			}
		}

		return read.trim();
	}
	public static Vector<String> format(Vector<String> data){
		Vector<String> newData = new Vector<>();
		int size = data.size();
		for(int i=0;i<size;i++) {
			String line = data.get(i);
			String lolo = "";
			if(line.contains("<")) {
			for(int j=0;j<line.length();j++) {
				if(line.charAt(j) == '<') {
					newData.add(lolo);
					lolo = "";
					for(int k = j;k<line.length();k++,j++) {
						if(line.charAt(k) == '>') {
							lolo+='>';
							newData.add(lolo);
							lolo = "";
							break;
						}else lolo+=line.charAt(k);
					}
				}else lolo+=line.charAt(j);
			}
			}else newData.add(line);
		}
		
		return newData;
	}
	public static void createFollowing(Vector<User> data){
		HashMap<Integer,ArrayList<Integer>> map = new HashMap<>();
		for(int i = 0;i<data.size();i++) {
			User user = data.get(i);
			ArrayList<Integer> follower = user.getFollowers();
			for(Integer e : follower) {
				if(!map.containsKey(e))
					map.put(e, new ArrayList<>());
				map.get(e).add(user.getId());
			}
		}
		for(int i = 0;i<data.size();i++) {
			
			User user = data.get(i);
			if(map.containsKey(user.getId())) {
				user.setFollowing(map.get(user.getId()));
			}
		}
		
	}
	public static String showMostInfluencer(Vector<User> data) {
		String ans = "";
		int max = 0;
		for(int i=0;i<data.size();i++) {
			User user = data.get(i);
			max = Math.max(max, user.getFollowers().size());
		}
		for(int i=0;i<data.size();i++) {
			User user = data.get(i);
			if(user.getFollowers().size() == max) {
				ans += "User: " + user.getName() + " ID:" + user.getId();
				ans+="\n";
			}
		}
		
		return ans;
	}
	public static String showMostActive(Vector<User> data) {
		String ans = "";
		int max = 0;
		for(int i=0;i<data.size();i++) {
			User user = data.get(i);
			max = Math.max(max, user.getFollowing().size());
		}
		for(int i=0;i<data.size();i++) {
			User user = data.get(i);
			if(user.getFollowing().size() == max) {
				ans += "User: " + user.getName() + " ID:" + user.getId();
				ans+="\n";
			}
		}
		
		return ans;
	}
	public static String mutualFollowers(int id1,int id2 ,Vector<User> data) {
		String ans = "";
		createHash(data);
		User user1 = null ,user2 = null;
		for(int i=0;i<data.size();i++) {
			User user = data.get(i);
			if(user.getId() == id1) user1 = user;
			else if(user.getId() == id2) user2 = user;
		}
		ArrayList<Integer> u1 = user1.getFollowers();
		ArrayList<Integer> u2 = user2.getFollowers();
		
		for(Integer e : u1) {
			if(u2.contains(e)) {
				ans+= "User " + userById.get(e).getName() + " ID: " + e;
				ans+="\n";
			}
		}
		return ans;
	}
	public static void createHash(Vector<User> data) {
		for(User e : data) {
			userById.put(e.getId(),e);
		}
	}
	public static String createLists(Vector<User> data) {
		String ans = "";
		createHash(data);
		for(int i=0;i<data.size();i++) {
			ArrayList<Integer> answer = new ArrayList<>();
			User user = data.get(i);
			ArrayList<Integer> follower = user.getFollowers();
			for(Integer e : follower) {
				User u = userById.get(e);
				if(u.getFollowers() != null) {
				ArrayList<Integer> fof = u.getFollowers();
				for(Integer ee : fof) {
					if(!answer.contains(ee) && ee != user.getId()) answer.add(ee);
				}
				}
			}
			ans+= "Suggested users of User: " + user.getName() + " ID: " + user.getId();
			ans+="\n";
			if(answer.size() > 0)
				for(Integer e : answer) {
					ans+= "User " + userById.get(e).getName() + " ID: " + e;
					ans+= "\n";
				}
			else ans+= "No Suggestion" + "\n";
		}
		
		return ans;
	}
	public static String postSearchWord(String word,Vector<User> data) {
		String ans = "";
		for(int i=0;i<data.size();i++) {
			User user = data.get(i);
			ArrayList<String> posts = user.getPosts();
			boolean ok = false;
			for(String e : posts) {
				if(e.contains(word)) {
					if(!ok) {
						ans+= "User " + user.getName() + " ID:" + user.getId();
						ans+="\n";
						ok = true;
					}
					ans+= e;
					ans+="\n";
				}
			}
		}
		return ans;
	}
	public static String postSearchTopic(String topic,Vector<User> data) {
		String ans = "";
		for(int i=0;i<data.size();i++) {
			User user = data.get(i);
			ArrayList<String> posts = user.getTopics();
			boolean ok = false;
			for(String e : posts) {
				if(e.contains(topic)) {
					if(!ok) {
						ans+= "User " + user.getName() + " ID:" + user.getId();
						ans+="\n";
						ok = true;
					}
					ans+= e;
					ans+="\n";
				}
			}
		}
		return ans;
	}
	public static void main(String[] args) {
		Vector<String> data = new Vector<>();
		Vector<String> fff = new Vector<>();
		//data.add("<user>");
		//data.add("<id>");
		data.add("<user><name>mohamed</name><id>2</id><body>omodsfsdfhuhfsdu</body></user>");
		//data.add("</user>");
		
		//ArrayList<Integer> users = user.get(0).
		//fff = format(data);
		//for(String e:fff)
		//System.out.println(user.get(0).getId());
		//-------------------------------------------------
			
	        FileInputStream fis = null;
	        BufferedReader reader = null;
	        Vector<String> file = new Vector<>();
	        try {
	            fis = new FileInputStream("C:\\Users\\DELL\\Videos\\sample.xml");
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
	        Vector<User> users = createGraph(file);
	        createFollowing(users);
	        /*for(User e : users) {
	        	/*System.out.println(e.getId());
	        	System.out.println(e.getName());
	        	ArrayList<Integer> koko = e.getFollowing();
	        	for(Integer g : koko) {
	        		System.out.print(g + " ");
	        	}
	        	System.out.println();
	        	/*ArrayList<String> soso = e.getPosts();
	        	for(String g : soso) {
	        		System.out.println(g);
	        	}
	        	System.out.println();
	        }*/
	     //   System.out.println(showMostInfluencer(users));
	     //   System.out.println(showMostActive(users));
	       System.out.println(postSearchTopic("economy",users));
	}
}

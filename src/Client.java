import java.io.*;
import java.net.*;

public class Client {
    public static void main(String args[]) {
        Socket socket = null;
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
            // create a new socket connected to the given address and port
            socket = new Socket("localhost", 5000);
            System.out.println("Connected");

            // create a new DataOutputStream to write data to the socket
            out = new DataOutputStream(socket.getOutputStream());

            // create a new BufferedReader to read data from the console
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            // create a new DataInputStream to read data from the socket
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String output= in.readUTF();
            System.out.println(output);
            boolean loggedIn = false;
            // read The Login Menu from the server
            while (true) {
                String login_menu = in.readUTF();
                System.out.println(login_menu);
                // read the user's input for username and password or - to exit
                String choice = console.readLine();
                out.writeUTF(choice);
                // read the server's response
                output = in.readUTF();
                System.out.println(output);
                if (output.contains("Closing")) {
                    break;
                }
                String userInfo = console.readLine();
                out.writeUTF(userInfo);
                output = in.readUTF();
                System.out.println(output);
                if(output.equals("logged in successfully") || output.equals("Player added successfully")){
                    loggedIn = true;
                    break;
                }
            }
            //menu for the game
            if(loggedIn){
                while (true){
                    String menu = in.readUTF();
                    System.out.println(menu);
                    String choice = console.readLine();
                    out.writeUTF(choice);
                    output = in.readUTF();
                    System.out.println(output);
                    if(output.contains("Starting Single Player Game")){
                        String any = console.readLine();
                        out.writeUTF(any);
                        while(true) {
                            String word = in.readUTF();
                            System.out.println(word);
                            String guess = console.readLine();
                            out.writeUTF(guess);
                            output = in.readUTF();
                            System.out.println(output);
                            if(output.contains("You won")){
                                break;
                            }
                            else if(output.contains("You lost")){
                                break;
                            } else if (output.contains("exited")) {
                                break;
                            }
                        }
                    }
                    else if(output.contains(("Starting Multi Player Game"))){
                        //read the choice of user
                        String choice2 = console.readLine();
                        out.writeUTF(choice2);
                        //read the response from the server
                        output = in.readUTF();
                        System.out.println(output);
                        if(output.contains("number of players in the team")) {
                            //read the number of players per team
                            String numPlayersPerTeam = console.readLine();
                            out.writeUTF(numPlayersPerTeam);
                            //read the response from the server
                            output = in.readUTF();
                            System.out.println(output);
                            //read the team name
                            while (true) {
                                String teamName = console.readLine();
                                out.writeUTF(teamName);
                                //check if the team name is valid or not
                                output = in.readUTF();
                                System.out.println(output);
                                if (output.contains("valid")) {
                                    break;
                                }
                            }
                        }
                        else if(output.contains("Join a team")){

                        }
                    }
                    else if(output.contains("History")){
                        String any = console.readLine();
                        out.writeUTF(any);
                        String history = in.readUTF();
                        System.out.println(history);
                    }
                    else if (output.contains("exit")) {
                        break;
                    }
                    else if(output.contains("Invalid choice")){
                        continue;
                    }
                }
            }
            out.close();
            socket.close();
        } catch(UnknownHostException u) {
            System.out.println(u);
        } catch(IOException i) {
            System.out.println(i);
        }
    }
}
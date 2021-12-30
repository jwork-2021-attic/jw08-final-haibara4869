package nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.awt.event.KeyEvent;

import screen.*;
import world.*;
import asciiPanel.*;
/**
 * 
 * This is a simple NIO based server.
 *
 */
public class Server extends Thread{
	private Selector selector;

	private InetSocketAddress listenAddress;
	private final static int PORT = 1234;

	private List<String> messages0;
	private List<String> messages1;
	private World world;
	private Olderman olderman;
    private Player player;
	private Player player1;
    

	private void createWorld() {
        world = new WorldBuilder(30,30).makeOnlineCaves().build();
    }

	private void createCreatures(CreatureFactory creatureFactory) {
        //player
		Player player0 = new Player(this.world, (char)1, AsciiPanel.brightWhite, 100,100, 25, 5, 9,0);
        world.addSpecialLocation(player0,1,1);
        player0.setMessages(messages0);
        this.player = player0;
        Player player1 = new Player(this.world, (char)5, AsciiPanel.brightWhite, 100,100, 25, 5, 9,0);
        world.addSpecialLocation(player1,1,2);
        player1.setMessages(messages1);
        this.player1 = player1;
		creatureFactory.setPlayer0(this.player);
        //npc 
        this.olderman = creatureFactory.newOlderman();
        //enemy
        for(int i = 0;i<5;i++){
            creatureFactory.newFrog();
            //creatureFactory.newBat();
        }
        //creatureFactory.newFireCow();       
    }

	public Server(String address, int port) throws IOException {
		
		this.messages0 = new ArrayList<String>();
		this.messages1 = new ArrayList<String>();

		createWorld();
		CreatureFactory creatureFactory = new CreatureFactory(this.world);
        createCreatures(creatureFactory);
		listenAddress = new InetSocketAddress(address, PORT);
	}

	public static void main(String[] args) throws Exception {
		//int port = Integer.parseInt(args[0]);
		try {
			new Server("0.0.0.0", 1234).startServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Start the server
	 * 
	 * @throws IOException
	 */
	private void startServer() throws IOException {
		this.selector = Selector.open();
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);

		// bind server socket channel to port
		serverChannel.socket().bind(listenAddress);
		serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		System.out.println("Server started on port >> " + PORT);
		ByteBuffer buffer = ByteBuffer.allocate(8192);
		int playernum = 0;
		while (true) {
			
			// wait for events
			int readyCount = selector.select();
			if (readyCount == 0) {
				continue;
			}

			
			// process selected keys...
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator iterator = readyKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = (SelectionKey) iterator.next();

				// Remove key from set so we don't process it twice
				iterator.remove();

				if (!key.isValid()) {
					continue;
				}

				if (key.isAcceptable()) {
					System.out.println("playernum" + playernum);
                    SocketChannel client = serverChannel.accept();
                    client.configureBlocking(false);
                    client.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                    client.keyFor(this.selector).attach(playernum);
                    playernum+=1;
                    System.out.println("connection with" + client.socket());
				} else if (key.isReadable()) { // Read from client
					try { 
						Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace();
					}
					SocketChannel client = (SocketChannel) key.channel();
                    int index = (Integer) key.attachment();
                    buffer.clear();
                    client.read(buffer);
					buffer.flip();
					System.out.println("Receive from" + client.socket());
                    while (buffer.remaining() >= 4) {
                        int KeyCode = buffer.getInt();
						System.out.println("buffer:" + KeyCode);
                        if(index==0){
							//System.out.println("player:" + player.x()+","+player.y());
							player.respondToUserInput(KeyCode);
							//System.out.println("player:" + player.x()+","+player.y());
						}
						else{
							player1.respondToUserInput(KeyCode);
						}
                    }
                    
				} else if (key.isWritable()) {
					try { 
						Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace();
					}
					SocketChannel client = (SocketChannel) key.channel();
                    int index = (Integer) key.attachment();
					//System.out.println(index);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream os = new ObjectOutputStream(baos);
                    os.writeObject(Packaged(index));
                    os.close();
                    ByteArrayOutputStream tempos = new ByteArrayOutputStream();
                    addInt2ByteArrayOS(tempos, baos.size());
                    tempos.writeBytes(baos.toByteArray());
                    client.write(ByteBuffer.wrap(tempos.toByteArray()));
                    //System.out.println("WRITE to" + client.socket());
                    baos.close();
                    tempos.close();
				}
			}
		}
		
	}

	public MyPackage Packaged(int index){
		//if (world != null && player != null)
        //    System.out.println("hp:"+player.hp());
		if(index==0){
			return new MyPackage(world,player,player1,this.olderman);
		}
		else{
			return new MyPackage(world,player1,player,this.olderman);
		}
	}
	
	public static void addInt2ByteArrayOS(ByteArrayOutputStream baos, int value) {
        baos.write(value >> 24);
        baos.write(value >> 16);
        baos.write(value >> 8);
        baos.write(value);
    }
	
}


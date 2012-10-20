package communication;

import java.util.Map;

import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;
import societe_virtuelle.Living;

public class SocketManager
{
	//! ATTRIBUTES
	private Living owner;
	private Map<Integer, Socket> sockets;
	
	//! METHODS
	
	// constructors
	public SocketManager(Living owner)
	{
		this.owner = owner;
	}
	
	// communication
	public void newConnexion(Message message, int destination_id)
	{
		Socket local_socket = new Socket(this, destination_id);
		sockets.put(local_socket.getConnexionId(), local_socket);
		local_socket.openConnexionWith(message);
	}
	
	@Watch(watcheeClassName = "communication.Socket",
			watcheeFieldNames = "open",
			triggerCondition = "$watchee.destination_id == $watcher.owner.id",
			whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	private void receive(Socket sender)
	{
		int connexion_id = sender.getConnexionId(),
				sender_id = sender.getManager().owner.getId();
		Socket local_socket = sockets.get(connexion_id);
		
		// is this a conversation already in progress?
		if(local_socket != null)
			local_socket.treatMessage(sender.getMessage());
		// if not we need to create a new socket for this conversation
		else
			sockets.put(connexion_id, new Socket(this, sender_id));
	}
}

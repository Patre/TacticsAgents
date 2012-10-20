/*
 Copyright (C) 2012 William James Dyce

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package communication;


/**
 * Class deals with a sending a message and receiving the reply, if there is one
 * to receive: it negotiates on behalf of its owner.
 * 
 * @author wdyce
 * @since 20 October, 2012
 * @version 0.1
 */

public class Socket
{
	//! CLASS VARIABLES
	static private int next_connexion_id = 0;
	
	//! ATTRIBUTES
	private final SocketManager manager;
	private final int destination_id;
	private final int connexion_id;
	private boolean open = false;
	private Message tendered_message = null;
	
	
	//! METHODS
	
	// constructors
	public Socket(SocketManager manager, int destination_id)
	{
		this(manager, destination_id, next_connexion_id++);
	}
	
	public Socket(SocketManager manager, int destination_id, int connexion_id)
	{
		this.manager = manager;
		this.destination_id = destination_id;
		this.connexion_id = connexion_id;
	}
	
	// communication
	
	public void openConnexionWith(Message message)
	{
		open = true;
		tendered_message = message;
	}
	
	public void treatMessage(Message message)
	{
		System.out.println("RECEIVED A MESSAGE!");
	}
	
	// accessors
	public int getConnexionId() { return connexion_id; }
	public SocketManager getManager() { return manager; }
	public Message getMessage() 
	{ 
		Message return_value = tendered_message;
		tendered_message = null;
		return return_value;
	}
}

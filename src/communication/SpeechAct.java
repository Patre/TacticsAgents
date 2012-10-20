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

public class SpeechAct
{
	
	//! ATTRIBUTES
	private final int sender_id;
	
	
	//! METHODS
	
	// 
	public SpeechAct(int sender_id)
	{
		this.sender_id = sender_id;
		
		send(new Locution(sender_id, Locution.Type.EXPRESSIVE));
	}
	
	//! SUBROUTINES
	
	public void send(Locution msg)
	{
		// message will be received based on annotations
	}
}

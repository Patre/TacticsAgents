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

import math.V2;

/**
 * A message sent from one agent to another, with a type value so that it can be
 * caste to the appropriate sub-class.
 * 
 * @author wdyce
 * @since 20 October, 2012
 * @version 0.1
 */
public class Message
{
	// ! NESTING

	/**
	 * Type of message, so that we can caste it to the appropriate sub-class.
	 */
	public static enum Type
	{
		/**
		 * "Speech acts that commit a speaker to the truth of the expressed
		 * proposition, e.g. reciting a creed."
		 */
		ASSERTIVE,
		/**
		 * "Speech acts that are to cause the hearer to take a particular action,
		 * e.g. requests, commands and advice."
		 */
		DIRECTIVE,
		/**
		 * "Speech acts that commit a speaker to some future action, e.g. promises
		 * and oaths."
		 */
		COMMISSIVE,
		/**
		 * "Speech acts that express the speaker's attitudes and emotions towards 
		 * the proposition, e.g. congratulations, excuses and thanks."
		 */
		EXPRESSIVE,
		/**
		 * "Speech acts that change the reality in accord with the proposition of 
		 * the declaration, e.g. baptisms, pronouncing someone guilty or pronouncing
		 * someone husband and wife."
		 */
		DECLARATIVE
	}

	
	// ! ATTRIBUTES
	
	/**
	 * The sender's unique identifier.
	 */
	private final int sender_id;
	
	/**
	 * Type of speech act, so that we can caste it to the appropriate sub-class.
	 */
	private final Type type;

	
	
	//! METHODS
	
	// constructors
	private Message(int sender_id, Type type)
	{
		this.type = type;
		this.sender_id = sender_id;
	}
}

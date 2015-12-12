package constructs;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;


//RSSFeed object which is written to by the DOMParser, containing
//different RSSItem objects and the ability to add another item.
//Also allows for finding the total number of items in the feed
//if unknown already.

//Original author Isaac Whitfield

public class RSSFeed implements Serializable {

	// Serializable ID
	private static final long serialVersionUID = 1L;

	// Create a new item list
	public List<RSSItem> _itemlist;

	// Create a new item count
    private int _itemcount = 0;

	// Initialize the item list
	RSSFeed() {
		_itemlist = new Vector<>(0);
	}

	void addItem(RSSItem item) {

		// Add an item to the Vector
		_itemlist.add(item);

		// Increment the item count
		_itemcount++;
	}

	public RSSItem getItem(int location) {

		// Return the item at the chosen position
		return _itemlist.get(location);
	}

	public int getItemCount() {

		// Return the number of items in the feed
		return _itemcount;

	}

}

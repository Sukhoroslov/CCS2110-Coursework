// ********************************************************
// Array-based implementation of the ADT list.
// *********************************************************
public class ArrayBasedList implements ListInterface
{
	private int MAX_SIZE = 2;
	private Object dataItems[];
	private int numItems;

	public ArrayBasedList()
	{
		dataItems = new Object[MAX_SIZE];
		numItems = 0;
	}
	
	public boolean isEmpty()
	{
		return (numItems == 0);
	}
   
	public int size()
	{
		return numItems;
	}

	public Object get( int index ) 
					  throws ListIndexOutOfBoundsException
	{
		if (index >= 1 && index <= numItems)
			return dataItems[translate(index)];
		else
		{
			throw new ListIndexOutOfBoundsException(
			"ListIndexOutOfBoundsException on get");
		}
	}

	public void add( int index, Object newDataItem )
					throws  ListIndexOutOfBoundsException
	{
		if (index < 1 || index > numItems+1)
			throw new ListIndexOutOfBoundsException("ListIndexOutOfBoundsException on add");

		if (numItems == MAX_SIZE)
			resize("expand");
		
		for (int pos = numItems; pos >= index; pos--)
			dataItems[translate(pos+1)] = dataItems[translate(pos)];

		dataItems[translate(index)] = newDataItem;
		numItems++;
	}

	public void remove( int index ) 
					   throws ListIndexOutOfBoundsException
	{
		if (index<1 || index>numItems)
		{
			throw new ListIndexOutOfBoundsException(
			"ListIndexOutOfBoundsException on remove");
		}
		
		for (int pos = index+1; pos <= size(); pos++)
		    dataItems[translate(pos-1)] = dataItems[translate(pos)];
		numItems--;

		if (numItems == (MAX_SIZE/2))
			resize("shrink");
	}
   
	private int translate( int position )
	{
		return position - 1;
	}

	private void resize(String allocation)
	{
		if (allocation == "expand")
			MAX_SIZE = MAX_SIZE * 2;
		else
			MAX_SIZE = MAX_SIZE / 2;

		Object [] newDataItems = new Object[MAX_SIZE];

			for (int i=0; i<numItems; i++)
				newDataItems[i] = dataItems[i];
			dataItems = newDataItems;
	}
}
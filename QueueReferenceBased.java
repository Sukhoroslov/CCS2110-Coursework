public class QueueReferenceBased implements QueueInterface
{
	private Node head;
	private Node tail;
  
	public QueueReferenceBased()
	{
		head = null;
		tail = null;
	}
  
	public boolean isEmpty()
	{
		return head == null;
	}

	public boolean isFull()
	{
		return false;
	}

	public void dequeueAll()
	{
		head = tail = null;
	}

	public void enqueue(Object newItem)
	{
		Node newNode = new Node(newItem);

		if (isEmpty())
			head = tail = newNode;
		else
		{
			tail.setNext(newNode);
			tail = newNode;
		}
	}

	public Object dequeue() throws QueueException
	{
		if (!isEmpty())
		{
			Object data = head.getItem();
			head = head.getNext();
			if (isEmpty())
				head = tail = null;
			return data;
		}
		else
			throw new QueueException("QueueException on dequeue:" + "queue empty");
	}

	public Object peek() throws QueueException
	{
		if (!isEmpty())
			return head.getItem();
		else
			throw new QueueException("QueueException on peek:" + "queue empty");
	}
}
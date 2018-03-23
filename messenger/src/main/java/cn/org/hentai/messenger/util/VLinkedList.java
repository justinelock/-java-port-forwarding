package cn.org.hentai.messenger.util;

import java.util.LinkedList;

/**
 * Created by matrixy on 2018/3/24.
 * 做一个简化版的链表，主要要避开使用lambda表达式
 */
public class VLinkedList<E>
{
    private int size = 0;
    private Node<E>
            first = null,              // 链表头
            last = null,               // 链表尾
            current = null;            // 当前位置

    public VLinkedList()
    {
        // ...
    }

    public int size()
    {
        return size;
    }

    public void add(E item)
    {
        final Node<E> l = last;
        Node<E> node = new Node<E>(last, item, null);
        last = node;
        if (null == l) first = node;
        else l.next = node;
        size++;
    }

    public void remove(E item)
    {
        Node<E> curr = first;
        while (curr != null)
        {
            if (item.equals(curr.item))
            {
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
                break;
            }
            curr = curr.next;
        }
    }

    public void traverse(ListAwalker awalker)
    {
        Node<E> curr = first;
        while (curr != null)
        {
            awalker.test(curr.item);
            curr = curr.next;
        }
    }

    public static interface ListAwalker<E>
    {
        public void test(E e);
    }

    private static class Node<T>
    {
        public Node prev, next;
        public T item;
        public Node(Node prev, T item, Node next)
        {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    public static void main(String[] args) throws Exception
    {
        String a, b, c, d, e, f, g, h, i, j, k, l;
        VLinkedList list = new VLinkedList();
        ListAwalker<String> awalker = new ListAwalker<String>() {
            @Override
            public void test(String s)
            {
                System.out.println("Test: " + s);
            }
        };
        list.add(a = new String("a"));
        list.add(b = new String("b"));
        list.add(c = new String("c"));
        list.add(d = new String("d"));
        list.add(e = new String("e"));
        list.add(f = new String("f"));
        list.add(g = new String("g"));
        list.add(h = new String("h"));
        list.add(i = new String("i"));
        list.add(j = new String("j"));
        list.add(k = new String("k"));
        list.add(l = new String("l"));
        System.out.println("Size: " + list.size());
        list.traverse(awalker);
        System.out.println("------------------------------");
        list.traverse(new ListAwalker<String>()
        {
            @Override
            public void test(String o)
            {
                if ("k".equals(o)) list.remove(o);
            }
        });
        list.traverse(awalker);
    }
}
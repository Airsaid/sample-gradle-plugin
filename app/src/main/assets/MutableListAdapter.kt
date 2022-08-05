import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * This is a RecyclerView list adapter. We use DiffUtil and delegate this adapter by MutableList
 * So it support all the functions like the List.
 *
 * The functions below will notify the change with the RecyclerView
 * @see setItems
 * @see add
 * @see remove
 * @see addAll
 * @see removeAll
 * ...
 * However, We may swap the list content with sync the change.
 * @see swapList
 *
 */
abstract class MutableListAdapter<T, VH : RecyclerView.ViewHolder>(
    private val items: MutableList<T> = ArrayList(),
) : RecyclerView.Adapter<VH>(), MutableList<T> by items {

    private var itemClickListener: OnItemClickListener<T>? = null

    fun setItems(newItems: List<T>): Unit = delegateAndDiff {
        clear()
        addAll(newItems)
    }

    private fun <E> delegateAndDiff(block: MutableList<T>.() -> E): E {
        val old = ArrayList(items)
        val result = items.block()
        onCalculateDiff(old, items)
        calculateDiff(old, items, ::compareItem).dispatchUpdatesTo(this)
        return result
    }

    open fun onCalculateDiff(old: List<T>, items: List<T>) = Unit

    open fun <T> calculateDiff(
        oldItems: List<T>,
        newItems: List<T>,
        sameItems: (checkContent: Boolean, a: T, b: T) -> Boolean
    ) = DiffUtil.calculateDiff(createSimpleDiff(oldItems, newItems, sameItems))

    private fun <T> createSimpleDiff(
        oldItems: List<T>,
        newItems: List<T>,
        sameItems: (checkContent: Boolean, a: T, b: T) -> Boolean
    ) = object : DiffUtil.Callback() {
        override fun getOldListSize() = oldItems.size
        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            sameItems(false, oldItems[oldItemPosition], newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            sameItems(true, oldItems[oldItemPosition], newItems[newItemPosition])
    }

    override fun add(element: T): Boolean = delegateAndDiff { add(element) }
    override fun remove(element: T): Boolean = delegateAndDiff { remove(element) }
    override fun addAll(elements: Collection<T>): Boolean = delegateAndDiff { addAll(elements) }
    override fun addAll(index: Int, elements: Collection<T>): Boolean =
        delegateAndDiff { addAll(index, elements) }

    override fun removeAll(elements: Collection<T>): Boolean =
        delegateAndDiff { removeAll(elements) }

    override fun retainAll(elements: Collection<T>): Boolean =
        delegateAndDiff { retainAll(elements) }

    override fun clear() = delegateAndDiff { clear() }
    override fun set(index: Int, element: T): T = delegateAndDiff { set(index, element) }
    override fun add(index: Int, element: T) = delegateAndDiff { add(index, element) }
    override fun removeAt(index: Int): T = delegateAndDiff { removeAt(index) }

    abstract fun compareItem(checkContent: Boolean, first: T, second: T): Boolean

    protected fun getItemAtPosition(position: Int) = items[position]

    override fun getItemCount() = items.size

    /**
     * Swap the original list without sync the change to the view.
     */
    fun swapList(newList: List<T>) {
        items.clear()
        items.addAll(newList)
    }

    fun snapshot(): MutableList<T> {
        return ArrayList(items)
    }

    fun dispatchItemClickEvent(v: View, position: Int) {
        itemClickListener?.onItemClick(this, v, position)
    }

    fun setOnItemClickListener(listener: OnItemClickListener<T>) {
        itemClickListener = listener
    }

    interface OnItemClickListener<T> {
        fun onItemClick(list: List<T>, v: View, position: Int)
    }

}
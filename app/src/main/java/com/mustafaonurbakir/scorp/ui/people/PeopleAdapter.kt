package com.mustafaonurbakir.scorp.ui.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mustafaonurbakir.scorp.data.Person
import com.mustafaonurbakir.scorp.databinding.PersonCardBinding
import java.util.*

class PeopleAdapter: RecyclerView.Adapter<PeopleViewHolder>() {

    private val items = ArrayList<Person>()

    fun setItems(items: ArrayList<Person>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val binding: PersonCardBinding = PersonCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeopleViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) = holder.bind(items[position])
}



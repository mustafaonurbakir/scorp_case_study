package com.mustafaonurbakir.scorp.ui.people

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mustafaonurbakir.scorp.data.Person
import com.mustafaonurbakir.scorp.databinding.PersonCardBinding

class PeopleViewHolder(
    private val itemBinding: PersonCardBinding
    ) : RecyclerView.ViewHolder(itemBinding.root), View.OnClickListener {

    private lateinit var person: Person

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: Person) {
        this.person = item
        itemBinding.name.text = item.fullName
        itemBinding.speciesAndStatus.text = """id: ${item.id}"""
    }

    override fun onClick(v: View?) {
        //click action
    }
}

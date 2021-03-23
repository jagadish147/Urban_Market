package com.jagadish.freshmart.view.address.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jagadish.freshmart.base.listeners.RecyclerAddressItemListener
import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.databinding.ViewAddressItemBinding
import com.jagadish.freshmart.view.address.AdressViewModel

/**
 * Created by AhmedEltaher
 */

class AddressAdapter(private val recipesListViewModel: AdressViewModel, private val recipes: List<AddAddressReq>) : RecyclerView.Adapter<AddressViewHolder>() {

    private val onItemClickListener: RecyclerAddressItemListener = object :
        RecyclerAddressItemListener {

        override fun onItemSelected(recipe: AddAddressReq,position : Int) {
            for(item in recipes){
                item.defaultAddress = false
            }
            recipes[position].defaultAddress = true
            notifyDataSetChanged()
            recipesListViewModel.openRecipeDetails(recipe)
        }

        override fun onItemRemoveAddress(addressReq: AddAddressReq, position: Int) {
            recipes.drop(position)
            notifyDataSetChanged()
            recipesListViewModel.removeAddress(addressReq)
        }

        override fun onItemEditAddress(addressReq: AddAddressReq, position: Int) {
            recipesListViewModel.updateAddressNavigate(addressReq)
        }


    }
    //https://jsonblob.com/add55310-7c87-11eb-981f-b9bef68ee992
    //https://jsonblob.com/api/add55310-7c87-11eb-981f-b9bef68ee992
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val itemBinding = ViewAddressItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun getSelectedItems() : List<AddAddressReq>{
        return recipes
    }
}


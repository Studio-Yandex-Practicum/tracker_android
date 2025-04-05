package com.nemislimus.tratometr.expenses.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nemislimus.tratometr.common.util.BindingFragment
import com.nemislimus.tratometr.databinding.FragmentCreateCategoryBinding

class CreateCategoryFragment : BindingFragment<FragmentCreateCategoryBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCreateCategoryBinding {
        return FragmentCreateCategoryBinding.inflate(inflater,container,false)
    }

}
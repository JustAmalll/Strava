package com.skillbox.strava.ui.fragment.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.skillbox.core.extensions.*
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.snackbar.CustomSnackBar
import com.skillbox.shared_model.ToastModel
import com.skillbox.shared_model.network.Athlete
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentProfileBinding
import com.skillbox.strava.ui.activity.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment :
    ViewBindingFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    override val screenViewModel by viewModels<ProfileViewModel>()
    private var userId: Long = 0
    override var setLogout = true
    override val setToolbar = true
    override var resToolbarId = R.string.profile_title_app

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        subscribe()
        screenViewModel.getAthlete()
    }

    override fun localData(localToast: ToastModel) {
        if (localToast.text.isBlank()) return
        CustomSnackBar.make(
            requireActivity().window.decorView.rootView as ViewGroup,
            localToast.isLocal,
            localToast.text,
            localToast.isError
        ) {
            screenViewModel.getAthlete()
        }.show()
    }

    private fun bind() {
        binding.profileButtonLogout.setOnClickListener {
            val action = ProfileFragmentDirections.actionHomeFragmentToLogOutDialogFragment()
            findNavController().navigate(action)
        }
        binding.profileButtonShare.setOnClickListener {
            val action = ProfileFragmentDirections.actionHomeFragmentToContactFragment(userId)
            findNavController().navigate(action)
        }
    }

    private fun subscribe() {
        screenViewModel.athleteObserver.observe(viewLifecycleOwner, { athlete ->
            athlete?.let { setData(athlete) }
        })
        screenViewModel.reAuthStateObserver.observe(viewLifecycleOwner, { isSuccessReAuth ->
            isSuccessReAuth?.let {
                if (isSuccessReAuth) {
                    startActivity(Intent(requireActivity(), OnBoardingActivity::class.java))
                    finishAffinity(requireActivity())
                }
            }
        })
        screenViewModel.loadDataObserver.observe(viewLifecycleOwner, { isLoad ->
            isLoad?.let {
                if (isLoad) {
                    visibleViewForm(false)
                    binding.profileLoader.show()
                } else {
                    visibleViewForm(true)
                    binding.profileLoader.gone()
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setData(model: Athlete) {
        userId = model.id
        model.profile?.let {
            Glide.with(requireContext())
                .load(model.profile)
                .placeholder(R.drawable.ic_placeholder_contact)
                .error(R.drawable.ic_error_contact)
                .transform(CircleCrop())
                .into(binding.profileImageViewPhoto)
        }
        binding.profileTvName.text = "${model.lastname} ${model.firstname}"
        binding.profileTvCountFollowers.text = "${model.follower ?: 0}"
        binding.profileTvCountFollowing.text = "${model.friend ?: 0}"
        when (model.sex?.name) {
            "M" -> {
                binding.profileTvGenderValue.text = getString(R.string.male)
            }
            "F" -> {
                binding.profileTvGenderValue.text = getString(R.string.female)
            }
            else -> {
                binding.profileTvGenderValue.text = getString(R.string.gender_not_selected)
            }
        }
        binding.profileTvCountryValue.text =
            model.country ?: getString(R.string.county_not_selected)
        setSpinner(model.weight?.toInt() ?: 0)
    }

    private fun setSpinner(currentWeight: Int) {
        val weightData = emptyArray<String>().toMutableList()
        (29..120).forEach { weight ->
            weightData.add("$weight kg")
        }
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            weightData.toList()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.profileSpinnerItems.adapter = adapter
        binding.profileSpinnerItems.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val weightSelected = weightData.toList()[position].split(' ').first().toInt()
                    screenViewModel.changeWeight(weightSelected)
                }

            }
        if (currentWeight != 0) {
            val index =
                weightData.toList().indexOfFirst { x -> x.contains(currentWeight.toString()) }
            binding.profileSpinnerItems.setSelection(index)
        }
    }

    private fun visibleViewForm(isVisible: Boolean) {
        if (isVisible.not()) {
            binding.linearLayout.gone()
            binding.linearLayout2.gone()
            binding.profileButtonLogout.gone()
        } else {
            binding.linearLayout.show()
            binding.linearLayout2.show()
            binding.profileButtonLogout.show()
        }
    }
}
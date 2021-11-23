package com.skillbox.strava.ui.fragment.activities

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.skillbox.core.extensions.gone
import com.skillbox.core.extensions.setData
import com.skillbox.core.extensions.show
import com.skillbox.core.platform.ViewBindingFragment
import com.skillbox.core.snackbar.CustomSnackBar
import com.skillbox.core_db.pref.Pref
import com.skillbox.shared_model.ToastModel
import com.skillbox.shared_model.network.СreateActivity
import com.skillbox.strava.R
import com.skillbox.strava.databinding.FragmentActivitiesBinding
import com.skillbox.strava.ui.fragment.activities.adapter.itemRunnerCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivitiesFragment :
    ViewBindingFragment<FragmentActivitiesBinding>(FragmentActivitiesBinding::inflate) {

    override val screenViewModel by viewModels<ActivitiesViewModel>()

    override var setLogout = true
    override val setToolbar = true
    override var resToolbarId = R.string.activities_title_app

    private val runnerCardAdapter by lazy {
        ListDelegationAdapter(
            itemRunnerCard(Pref(requireContext(), requireActivity().application))
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribe()
        bind()
        initList()
    }

    override fun onStart() {
        super.onStart()
        screenViewModel.getAthleteActivities()
    }

    override fun localData(localToast: ToastModel) {
        if (localToast.text.isBlank()) return
        CustomSnackBar.make(
            requireActivity().window.decorView.rootView as ViewGroup,
            localToast.isLocal,
            localToast.text,
            localToast.isError
        ) {
            screenViewModel.getAthleteActivities()
        }.show()
    }

    private fun subscribe() {
        screenViewModel.runnerItemsObserver.observe(viewLifecycleOwner, { list ->
            list?.let {
                setAdapter(list)
            }
        })
        screenViewModel.loadDataObserver.observe(viewLifecycleOwner, { isLoad ->
            isLoad?.let {
                if (isLoad) {
                    binding.activitiesLoader.show()
                    binding.activitiesRecyclerView.gone()
                } else {
                    binding.activitiesLoader.gone()
                    binding.activitiesRecyclerView.show()
                }
            }
        })
    }

    private fun bind() {
        binding.activitiesFabButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_activitiesFragment_to_addActivitiesFragment)
        }
    }

    private fun initList() {
        with(binding.activitiesRecyclerView) {
            adapter = runnerCardAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun setAdapter(list: List<СreateActivity>) {
        runnerCardAdapter.setData(list)
    }
}
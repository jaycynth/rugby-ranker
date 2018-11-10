package com.ricknout.rugbyranker.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ricknout.rugbyranker.R
import com.ricknout.rugbyranker.common.util.DateUtils
import com.ricknout.rugbyranker.util.FlagUtils
import com.ricknout.rugbyranker.vo.WorldRugbyMatch
import kotlinx.android.synthetic.main.list_item_world_rugby_match.view.*

class WorldRugbyMatchPagedListAdapter(
        private val showScores: Boolean,
        private val showTime: Boolean,
        private val onItemCountChange: () -> Unit
) : PagedListAdapter<WorldRugbyMatch, WorldRugbyMatchViewHolder>(DIFF_CALLBACK) {

    private var itemCount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = WorldRugbyMatchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_world_rugby_match, parent, false))

    override fun onBindViewHolder(holder: WorldRugbyMatchViewHolder, position: Int) {
        val worldRugbyMatch = getItem(position) ?: return
        holder.bind(worldRugbyMatch, showScores, showTime)
    }

    override fun getItemCount(): Int {
        val itemCount = super.getItemCount()
        if (this.itemCount != itemCount) {
            onItemCountChange.invoke()
            this.itemCount = itemCount
        }
        return itemCount
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<WorldRugbyMatch>() {
            override fun areItemsTheSame(oldItem: WorldRugbyMatch, newItem: WorldRugbyMatch) = oldItem.matchId == newItem.matchId
            override fun areContentsTheSame(oldItem: WorldRugbyMatch, newItem: WorldRugbyMatch) = oldItem == newItem
        }
    }
}

class WorldRugbyMatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(worldRugbyMatch: WorldRugbyMatch, showScores: Boolean, showTime: Boolean) {
        val firstTeamFlag = FlagUtils.getFlagEmojiForTeamAbbreviation(worldRugbyMatch.firstTeamAbbreviation ?: "")
        val secondTeamFlag = FlagUtils.getFlagEmojiForTeamAbbreviation(worldRugbyMatch.secondTeamAbbreviation ?: "")
        val teams = if (showScores) {
            itemView.context.getString(R.string.text_match_teams_with_scores,
                    firstTeamFlag, worldRugbyMatch.firstTeamName, worldRugbyMatch.firstTeamScore, worldRugbyMatch.secondTeamScore, worldRugbyMatch.secondTeamName, secondTeamFlag)
        } else {
            itemView.context.getString(R.string.text_match_teams,
                    firstTeamFlag, worldRugbyMatch.firstTeamName, worldRugbyMatch.secondTeamName, secondTeamFlag)
        }
        itemView.teamsTextView.text = teams
        if (showTime) {
            val time = DateUtils.getDate(DateUtils.DATE_FORMAT_HH_MM, worldRugbyMatch.timeMillis)
            itemView.timeTextView.text = time
            itemView.timeTextView.isVisible = true
        } else {
            itemView.timeTextView.text = null
            itemView.timeTextView.isVisible = false
        }
        itemView.eventTextView.text = worldRugbyMatch.eventLabel
        itemView.eventTextView.isVisible = worldRugbyMatch.eventLabel != null
        when {
            worldRugbyMatch.venueName != null && worldRugbyMatch.venueCountry != null -> {
                val venueAndCountry = itemView.context.getString(R.string.text_match_venue_country, worldRugbyMatch.venueName, worldRugbyMatch.venueCountry)
                itemView.venueTextView.text = venueAndCountry
                itemView.venueTextView.isVisible = true
            }
            worldRugbyMatch.venueName != null -> {
                itemView.venueTextView.text = worldRugbyMatch.venueName
                itemView.venueTextView.isVisible = true
            }
            worldRugbyMatch.venueCountry != null -> {
                itemView.venueTextView.text = worldRugbyMatch.venueCountry
                itemView.venueTextView.isVisible = true
            }
            else -> {
                itemView.venueTextView.text = null
                itemView.venueTextView.isVisible = false
            }
        }
    }
}

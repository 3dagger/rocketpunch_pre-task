package kr.dagger.rocketpunchpretask.presentation.ui.message.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.dagger.domain.model.Message
import kr.dagger.rocketpunchpretask.databinding.ItemMessageListBinding
import kr.dagger.rocketpunchpretask.databinding.ItemMessageReceivedBinding
import kr.dagger.rocketpunchpretask.databinding.ItemMessageSentBinding
import kr.dagger.rocketpunchpretask.databinding.ItemSearchUserListBinding
import java.lang.Exception

class ChatListAdapter constructor(
	private val viewModel: ChatViewModel,
	private val userId: String
) : ListAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()){

	private val typeMessageReceived = 1
	private val typeMessageSent = 2

	override fun getItemViewType(position: Int): Int {
		return if (getItem(position).senderId != userId) {
			typeMessageReceived
		} else {
			typeMessageSent
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)

		return when (viewType) {
			typeMessageSent -> ChatSentViewHolder(ItemMessageSentBinding.inflate(layoutInflater, parent, false))
			typeMessageReceived -> ChatReceivedViewHolder(ItemMessageReceivedBinding.inflate(layoutInflater, parent, false))
			else -> throw Exception()
		}
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		when (holder.itemViewType) {
			typeMessageSent -> (holder as ChatSentViewHolder).bind(viewModel, getItem(position))
			typeMessageReceived -> (holder as ChatReceivedViewHolder).bind(viewModel, getItem(position))
		}
	}

	inner class ChatReceivedViewHolder(private val binding: ItemMessageReceivedBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(viewModel: ChatViewModel, msg: Message) {
			binding.apply {
				vm = viewModel
				data = msg

//				val halfHourMill = 1_800_000
//				val index =
//				txtReceivedTime
			}
		}
	}

	inner class ChatSentViewHolder(private val binding: ItemMessageSentBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(viewModel: ChatViewModel, msg: Message) {
			binding.apply {
				vm = viewModel
				data = msg
			}
		}
	}
}
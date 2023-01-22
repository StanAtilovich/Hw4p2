package com.example.hw4.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hw4.Calc
import com.example.hw4.Post
import com.example.hw4.R
import com.example.hw4.databinding.CardPostBinding

typealias OnLikeListener = (Post) -> Unit
typealias OnShareListener = (Post) -> Unit

class PostAdapter(
    private val likeClickListener: OnLikeListener,
    private val shareClickListener: OnShareListener,
) : ListAdapter<Post,PostViewHolder>(PostItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = (CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        return PostViewHolder(binding, likeClickListener,shareClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }


}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val likeClickListener: OnLikeListener,
    private val shareClickListener: OnShareListener,

) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            textLikes.text = Calc.intToText(post.likCount)
            textShares.text = Calc.intToShareText(post.shareCount)
            textViews.text = Calc.intToViewText(post.countView)

            likes.setImageResource(
                if (post.likedByMe) R.drawable.ic_baseline_favorite_24
                else R.drawable.ic_baseline_favorite_border_24
            )
            likes.setOnClickListener {
                 likeClickListener(post)
            }
            textLikes.text = post.likCount.toString()

            shares.setOnClickListener{
                shareClickListener(post)
            }

        }
    }
}
class PostItemCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
        oldItem == newItem


}
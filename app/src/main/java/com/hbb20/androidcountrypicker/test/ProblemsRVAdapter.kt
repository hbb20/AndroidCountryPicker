package com.hbb20.androidcountrypicker.test

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hbb20.androidcountrypicker.R

class ProblemsRVAdapter(
    context: Context,
    private val rvItems: List<ProblemRVItem>
) :
        RecyclerView.Adapter<ProblemVH>() {
    private val fileNameViewType = 0
    private val categoryNameType = 1
    private val problemInfoType = 2
    private val summaryViewType = 3
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProblemVH {
        val rowView = layoutInflater.inflate(
            when (viewType) {
                fileNameViewType -> R.layout.problem_filename_row
                categoryNameType -> R.layout.problem_category_row
                problemInfoType -> R.layout.problem_info_row
                summaryViewType -> R.layout.problem_summary_status_row
                else -> throw IllegalArgumentException("Unexpected viewType.")
            },
            parent,
            false
        )

        return when (viewType) {
            fileNameViewType -> ProblemFileVH(rowView)
            categoryNameType -> ProblemCategoryVH(rowView)
            problemInfoType -> ProblemInfoVH(rowView)
            summaryViewType -> SummaryVH(rowView)
            else -> throw IllegalArgumentException("Unexpected viewType.")
        }
    }

    override fun getItemCount(): Int {
        return rvItems.size
    }

    override fun onBindViewHolder(
        holder: ProblemVH,
        position: Int
    ) {
        when (holder) {
            is ProblemFileVH -> holder.bindItem(rvItems[position] as ProblemFileRVItem)
            is ProblemCategoryVH -> holder.bindItem(rvItems[position] as ProblemCategoryRVItem)
            is ProblemInfoVH -> holder.bindItem(rvItems[position] as ProblemInfoRVItem)
            is SummaryVH -> holder.bindItem(rvItems[position] as StatusSummary)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (rvItems[position]) {
            is ProblemFileRVItem -> fileNameViewType
            is ProblemCategoryRVItem -> categoryNameType
            is ProblemInfoRVItem -> problemInfoType
            is StatusSummary -> summaryViewType
        }
    }

}

sealed class ProblemRVItem
class ProblemFileRVItem(
    val fileName: String,
    val criticalProblemCount: Int,
    val nonCriticalProblemCount: Int
) : ProblemRVItem()

class StatusSummary(
    val errorCount: Int,
    val warningCount: Int
) : ProblemRVItem()

class ProblemCategoryRVItem(val categoryName: String) : ProblemRVItem()
class ProblemInfoRVItem(val problem: Problem) : ProblemRVItem()

sealed class ProblemVH(itemView: View) : RecyclerView.ViewHolder(itemView)
class ProblemFileVH(itemView: View) : ProblemVH(itemView) {
    val tvFileName = itemView.findViewById<TextView>(R.id.tvFileName)
    val tvErrorCount = itemView.findViewById<TextView>(R.id.tvCriticalCount)
    val imgStatusIcon = itemView.findViewById<ImageView>(R.id.imgStatusIcon)
    fun bindItem(problemFileRVItem: ProblemFileRVItem) {
        tvFileName.text = problemFileRVItem.fileName
        if (problemFileRVItem.criticalProblemCount == 0) {
            if (problemFileRVItem.nonCriticalProblemCount > 0) {
                imgStatusIcon.setImageResource(R.drawable.ic_warning)
            } else {
                imgStatusIcon.setImageResource(R.drawable.ic_check_circle_24dp)
            }
            tvErrorCount.visibility = View.GONE
        } else {
            imgStatusIcon.setImageResource(R.drawable.ic_error_outline)
            tvErrorCount.visibility = View.VISIBLE
            tvErrorCount.text = "${problemFileRVItem.criticalProblemCount}"
        }
    }
}

class ProblemCategoryVH(itemView: View) : ProblemVH(itemView) {
    private val tvCategoryName = itemView.findViewById<TextView>(R.id.tvCategory)
    fun bindItem(problemCategoryRVItem: ProblemCategoryRVItem) {
        tvCategoryName.text = problemCategoryRVItem.categoryName
    }
}

class ProblemInfoVH(itemView: View) : ProblemVH(itemView) {
    private val context = itemView.context
    private val tvProblem = itemView.findViewById<TextView>(R.id.tvProblem)
    private val imgProblemLevel = itemView.findViewById<ImageView>(R.id.imgProblemLevel)
    private val errorColor = ContextCompat.getColor(context, R.color.error_color)
    private val warningColor = ContextCompat.getColor(context, R.color.warning_color)

    fun bindItem(problemInfoRVItem: ProblemInfoRVItem) {
        when (problemInfoRVItem.problem.category) {
            ProblemCategory.UNVERIFIED_ENTRIES -> {
                tvProblem.setTextColor(warningColor)
                imgProblemLevel.setImageResource(R.drawable.ic_warning)
                imgProblemLevel.setColorFilter(warningColor)
            }
            else -> {
                tvProblem.setTextColor(errorColor)
                imgProblemLevel.setImageResource(R.drawable.ic_error_outline)
                imgProblemLevel.setColorFilter(errorColor)
            }
        }
        tvProblem.text = problemInfoRVItem.problem.solution
    }
}

class SummaryVH(itemView: View) : ProblemVH(itemView) {
    val imgStatus = itemView.findViewById<ImageView>(R.id.imgStatus)
    val tvCriticalErrorCount = itemView.findViewById<TextView>(R.id.tvCriticalSummaryCount)
    val tvNonCriticalErrorCount = itemView.findViewById<TextView>(R.id.tvNonCriticalCount)
    fun bindItem(statusSummary: StatusSummary) {
        val statusImageId = if (statusSummary.errorCount > 0) {
            R.drawable.ic_error_outline
        } else if (statusSummary.warningCount > 0) {
            R.drawable.ic_warning
        } else {
            R.drawable.ic_check_circle_24dp
        }

        imgStatus.setImageResource(statusImageId)
        tvCriticalErrorCount.text = statusSummary.errorCount.toString()
        tvNonCriticalErrorCount.text = statusSummary.warningCount.toString()
    }
}

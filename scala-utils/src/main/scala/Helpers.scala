package sctptp
import SequentCalculus.*
import FOL.*

object Helpers {


  given Conversion[SCProofStep, String] with {
    def apply(step: SCProofStep): String = step.name
  }

  given Conversion[Formula, Sequent] with {
    def apply(f: Formula): Sequent = () |- f
  }


  protected trait FormulaSeqConverter[T] {
      def apply(t: T): Seq[Formula]
    }

    given FormulaSeqConverter[Unit] with {
      override def apply(u: Unit): Seq[Formula] = Seq.empty
    }

    given FormulaSeqConverter[EmptyTuple] with {
      override def apply(t: EmptyTuple): Seq[Formula] = Seq.empty
    }

    given [H <: Formula, T <: Tuple](using c: FormulaSeqConverter[T]): FormulaSeqConverter[H *: T] with {
      override def apply(t: H *: T): Seq[Formula] = c.apply(t.tail) :+ t.head
    }

    given formula_to_Seq[T <: Formula]: FormulaSeqConverter[T] with {
      override def apply(f: T): Seq[Formula] = Seq(f)
    }

    given [T <: Formula, I <: Iterable[T]]: FormulaSeqConverter[I] with {
      override def apply(s: I): Seq[Formula] = s.toSeq
    }

    private def any2Seq[A, T <: A](any: T)(using c: FormulaSeqConverter[T]): Seq[Formula] = c.apply(any)

    extension [A, T1 <: A](left: T1)(using FormulaSeqConverter[T1]) {
      infix def |-[B, T2 <: B](right: T2)(using FormulaSeqConverter[T2]): Sequent = Sequent(any2Seq(left), any2Seq(right))
      infix def ⊢[B, T2 <: B](right: T2)(using FormulaSeqConverter[T2]): Sequent = Sequent(any2Seq(left), any2Seq(right))
    }

}

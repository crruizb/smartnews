import { Newspaper } from "lucide-react";
import { Link } from "react-router";

interface LogoProps {
  /** When true, only the mark is rendered (used in tight mobile spaces). */
  markOnly?: boolean;
}

export default function Logo({ markOnly = false }: LogoProps) {
  return (
    <Link
      to="/contributions"
      className="group flex items-center gap-2.5"
      aria-label="Smart News — home"
    >
      <span className="grid h-9 w-9 shrink-0 place-items-center rounded-xl bg-accent text-on-accent shadow-sm transition-transform duration-300 group-hover:-rotate-6">
        <Newspaper className="h-5 w-5" strokeWidth={2.2} aria-hidden="true" />
      </span>
      {!markOnly && (
        <span className="font-display text-[22px] font-semibold leading-none tracking-tight text-ink">
          Smart<span className="text-accent-strong">News</span>
        </span>
      )}
    </Link>
  );
}

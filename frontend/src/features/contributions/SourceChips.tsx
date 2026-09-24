interface SourceChipsProps {
  options: { value: string; label: string }[];
  value: string;
  onChange: (value: string) => void;
}

/** Horizontally scrollable source filter pills. */
export default function SourceChips({
  options,
  value,
  onChange,
}: SourceChipsProps) {
  return (
    <div
      role="group"
      aria-label="Filter by source"
      className="-mx-1 flex gap-2 overflow-x-auto px-1 pb-1 [scrollbar-width:none] [&::-webkit-scrollbar]:hidden"
    >
      {options.map((option) => {
        const isActive = option.value === value;
        return (
          <button
            key={option.value}
            type="button"
            onClick={() => onChange(option.value)}
            aria-pressed={isActive}
            className={`shrink-0 rounded-full border px-3.5 py-1.5 text-xs font-medium transition-colors cursor-pointer ${
              isActive
                ? "border-ink bg-ink text-paper"
                : "border-line bg-surface text-ink-2 hover:border-accent hover:text-accent-strong"
            }`}
          >
            {option.label}
          </button>
        );
      })}
    </div>
  );
}

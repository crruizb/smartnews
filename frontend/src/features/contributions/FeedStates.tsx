import type { ReactNode } from "react";
import { Loader2, TriangleAlert } from "lucide-react";
import { useTranslation } from "react-i18next";

interface StateProps {
  title: string;
  description?: string;
  icon?: ReactNode;
}

/** Centered empty/zero-results state. */
export function EmptyState({ title, description, icon }: StateProps) {
  return (
    <div className="mx-auto mt-8 flex max-w-md flex-col items-center rounded-2xl border border-dashed border-line px-6 py-12 text-center">
      {icon && (
        <div className="mb-4 grid h-12 w-12 place-items-center rounded-full bg-accent-soft text-accent-strong">
          {icon}
        </div>
      )}
      <p className="font-display text-lg font-semibold text-ink">{title}</p>
      {description && (
        <p className="mt-1.5 text-sm leading-relaxed text-muted">{description}</p>
      )}
    </div>
  );
}

/** Centered error state with a warning icon. */
export function ErrorState({ title, description }: StateProps) {
  return (
    <EmptyState
      title={title}
      description={description}
      icon={<TriangleAlert className="h-6 w-6" aria-hidden="true" />}
    />
  );
}

interface LoadMoreButtonProps {
  onClick: () => void;
  isFetching?: boolean;
}

export function LoadMoreButton({ onClick, isFetching }: LoadMoreButtonProps) {
  const { t } = useTranslation();
  return (
    <div className="mt-2 mb-4 flex justify-center">
      <button
        type="button"
        onClick={onClick}
        disabled={isFetching}
        className="inline-flex items-center gap-2 rounded-full bg-accent px-6 py-3 text-sm font-semibold text-on-accent transition-opacity hover:opacity-90 disabled:opacity-60 cursor-pointer"
      >
        {isFetching && (
          <Loader2 className="h-4 w-4 animate-spin" aria-hidden="true" />
        )}
        {t("loadMore", "Load more news")}
      </button>
    </div>
  );
}

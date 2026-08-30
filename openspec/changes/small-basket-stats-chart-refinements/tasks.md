## 1. First Render Sizing

- [x] 1.1 Update Basket stats view switching so the selected variation chart is redrawn after the Basket stats view becomes visible.
- [x] 1.2 Guard the redraw path so empty, loading, or missing basket stats selections keep their existing message/reset behavior.

## 2. Basket Chart Rendering

- [x] 2.1 Add Basket stats-specific rating tick generation at 20-rating-point intervals with whole-number labels.
- [x] 2.2 Add Basket stats-specific SPR ticks at `0`, `0.5`, `1`, and `1.5` with horizontal grid lines.
- [x] 2.3 Add Basket stats-specific VAR right-axis labels at `0.5` and `1` without drawing VAR grid lines.
- [x] 2.4 Rename the Basket stats X-axis label from `Rating midpoint` to `Rating`.

## 3. Bucket Styling

- [x] 3.1 Change Basket stats segment rendering so `50-99` bucket portions do not draw connecting lines.
- [x] 3.2 Change `100-199` bucket styling to a dotted line.
- [x] 3.3 Change `200+` bucket styling to a normal solid line.
- [x] 3.4 Preserve midpoint splitting between adjacent windows with different buckets, applying each endpoint bucket's line-or-dot rule to its half.

## 4. Tooltip Simplification

- [x] 4.1 Simplify Basket stats SPR point tooltip rows to show only rating and SPR value.
- [x] 4.2 Simplify Basket stats VAR point tooltip rows to show only rating and VAR value.
- [x] 4.3 Preserve multi-point hover behavior while keeping each tooltip row limited to metric name, rating, and that metric's value.

## 5. Verification

- [x] 5.1 Manually inspect the static page to confirm the first Basket stats chart render uses the same width as after changing variation.
- [x] 5.2 Manually inspect chart styling for `50-99`, `100-199`, and `200+` windows.
- [x] 5.3 Manually inspect Basket stats X-axis, SPR Y-axis, VAR Y-axis, and tooltip content against the spec.
- [x] 5.4 Run the applicable project compilation check.

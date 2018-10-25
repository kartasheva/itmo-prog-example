package ru.ifmo.prog.lab8;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class DrawablePane extends JPanel {
    private static final Double TICK = 10.0;
    private static final Double PX = 50.0;
    private static final Double TIME_DOWN = 5000.0;
    private static final Double TIME_UP = 3000.0;
    private static final Double STEP_DOWN = PX / (TIME_DOWN / TICK);
    private static final Double STEP_UP = PX / (TIME_UP / TICK);
    private Locale locale;

    private List<Cart> carts;
    private List<Cart> filteredCarts;
    private List<TimerEnd> timerEnds = new ArrayList<>();
    private CartFilters filters;
    private Timer timer;
    private Counetr counetr;

    public DrawablePane(List<Cart> carts) {
        super();
        ToolTipManager.sharedInstance().registerComponent(this);
        this.carts = carts;
        this.filteredCarts = carts;

        counetr = new Counetr();
        timer = new Timer(TICK.intValue(), e -> {
            counetr.appendToCounter(timer.getDelay());
            if (counetr.getCounter() <= TIME_DOWN) {
                filteredCarts
                        .forEach(cart -> cart.setY(cart.getY() + STEP_DOWN));
            } else if (counetr.getCounter() > TIME_DOWN && counetr.getCounter() <= (TIME_DOWN + TIME_UP)) {
                filteredCarts
                        .forEach(cart -> cart.setY(cart.getY() - STEP_UP));
            } else {
                timer.stop();
                counetr.clear();
                timerEnds.forEach(TimerEnd::handleTimerEnd);
            }
            repaint();
        });

        setPreferredSize(new Dimension(1000, 700));
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.clearRect(0, 0, getWidth(), getHeight());

        carts
                .stream()
                .filter(cart -> !checkCartByFilter(cart))
                .forEach(cart -> {
                    if (cart.getLabColor() != null) {
                        graphics2D.setColor(cart.getLabColor().toAWTColor());
                    }
                    graphics2D.fillRect(cart.getX().intValue(), cart.getY().intValue(), cart.getSize(), cart.getSize());
                });

        filteredCarts
                .forEach(cart -> {
                    if (cart.getLabColor() != null) {
                        graphics2D.setColor(cart.getLabColor().toAWTColor());
                    }
                    graphics2D.fillRect(cart.getX().intValue(), cart.getY().intValue(), cart.getSize(), cart.getSize());
                });
    }

    public void addTimerEndHandler(TimerEnd timerEnd) {
        timerEnds.add(timerEnd);
    }

    public void setCarts(List<Cart> carts) {
        this.carts = carts;
        this.filteredCarts = this.carts.stream().filter(this::checkCartByFilter).collect(Collectors.toList());
        this.repaint();
    }

    public void setFilters(CartFilters filters) {
        this.filters = filters;
        this.filteredCarts = this.carts.stream().filter(this::checkCartByFilter).collect(Collectors.toList());
    }

    public void start() {
        this.timer.start();
    }

    public void stop() {
        this.timer.stop();
    }

    private boolean checkCartByFilter(Cart cart) {
        if (filters.getTitle() != null && filters.getTitle().length() > 0) {
            return cart.getTitle().equals(filters.getTitle());
        }
        if (filters.isClatterOfHooves() != null) {
            return filters.isClatterOfHooves().equals(cart.isClatterOfHooves());
        }
        if (filters.getSize() != null) {
            return filters.getSize().equals(cart.getSize());
        }
        if (filters.getX() != null) {
            return filters.getX() >= cart.getX();
        }
        if (filters.getY() != null) {
            return filters.getY() >= cart.getY();
        }
        if (filters.getCreatedAt() != null) {
            return filters.getCreatedAt().equals(cart.getCreatedAt());
        }
        if (filters.getLabColor() != null) {
            return filters.getLabColor().equals(cart.getLabColor());
        }
        return true;
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        Optional<Cart> cart = carts
                .stream()
                .filter(_cart -> _cart.getX() <= event.getPoint().x
                        && event.getPoint().x <= _cart.getX() + _cart.getSize()
                        && _cart.getY() <= event.getPoint().y
                        && event.getPoint().y <= _cart.getY() + _cart.getSize())
                .findFirst();
        return cart.map(Cart::getTitle).orElse(null);
    }

    @Override
    public Point getToolTipLocation(MouseEvent event) {
        return event.getPoint();
    }
}
